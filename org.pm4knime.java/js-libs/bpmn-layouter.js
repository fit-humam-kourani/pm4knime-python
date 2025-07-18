class CustomWaypoint {
  dictio;
  $name;
  $model;
  $type;
  $attrs;
  $parent;
  $descriptor;
  x;
  y;

  constructor() {
    this.dictio = {};
  }

  get(varname) {
    return this.dictio[varname];
  }

  set(varname, varvalue) {
    this.dictio[varname] = varvalue;
  }
}

function renderGraph(
  iterativelyReachedNodes,
  nodes,
  edgesDict,
  nodesep,
  edgesep,
  ranksep,
  targetDivDagre,
  desideredWidth = null,
  desideredHeight = null
) {
  var g = new dagreD3.graphlib.Graph().setGraph({});

  for (let n of iterativelyReachedNodes) {
    if (!n.$type.toLowerCase().endsWith("flow")) {
      let name = "" + n.name;
      let isProperName = true;
      if (name.length == 0) {
        name = n.id;
        isProperName = false;
      }
      if (name == "start" || name == "end") {
        isProperName = false;
      }
      if (isProperName && desideredWidth != null) {
        g.setNode(n.id, {
          label: n.name.replaceAll(" ", "\n"),
          //width: desideredWidth,
          width: (n.name.length) * 6,
          height: desideredHeight,
        });
      } else if (desideredWidth != null) {
        g.setNode(n.id, {
          label: n.name.replaceAll(" ", "\n"),
          width: (n.name.length) * 6,
          //width: Math.min(desideredWidth, desideredHeight) * 0.28,
          height: Math.min(desideredWidth, desideredHeight) * 0.002,
        });
      } else {
        g.setNode(n.id, {
          label: n.name.replaceAll(" ", "\n"),
        });
      }
    }
  }

  for (let n of nodes) {
    if (n.$type.toLowerCase().endsWith("flow")) {
      let source = n.sourceRef.id;
      let target = n.targetRef.id;
      edgesDict[source + "@" + target] = n.id;
      g.setEdge(source, target, {
        label: "",
      });
    }
  }

  g.graph().rankDir = "LR";
  g.graph().nodesep = nodesep;
  g.graph().edgesep = edgesep;
  g.graph().ranksep = ranksep;

  let render = new dagreD3.render();
  let svg = d3.select("#" + targetDivDagre);
  let inner = svg.append("g");
  render(inner, g);

  return g;
}

async function bpmnLayoutWithDagre(xmlString) {
  let targetDivFirstBpmn = "internalCanvas";
  let targetDivDagre = "internalSvg";
  let nodesep = 30;
  let edgesep = 30;
  let ranksep = 85;

  let bpmnViewer = new BpmnJS({
    container: "#" + targetDivFirstBpmn,
  });

  await bpmnViewer.importXML(xmlString);

  let nodes = bpmnViewer._definitions.rootElements[0].flowElements;
  let graphical = bpmnViewer._definitions.diagrams[0].plane.planeElement;
  let graphicalDict = {};
  let edgesDict = {};
  let i = 0;
  while (i < graphical.length) {
    graphicalDict[graphical[i].bpmnElement.id] = i;
    i++;
  }

  let toVisit = [];
  let iterativelyReachedNodes = [];

  for (let n of nodes) {
    if (n.$type.toLowerCase().endsWith("startevent")) {
      toVisit.push(n);
      break;
    }
  }

  while (toVisit.length > 0) {
    let el = toVisit.pop();
    if (!iterativelyReachedNodes.includes(el)) {
      iterativelyReachedNodes.push(el);
    }
    if (el.outgoing != null) {
      for (let out of el.outgoing) {
        if (!iterativelyReachedNodes.includes(out.targetRef)) {
          toVisit.push(out.targetRef);
        }
      }
    }
  }

  let g = renderGraph(
    iterativelyReachedNodes,
    nodes,
    edgesDict,
    nodesep,
    edgesep,
    ranksep,
    targetDivDagre
  );

  let desideredWidth = 0;
  let desideredHeight = 0;

  for (let nodeId in g._nodes) {
    let node = g._nodes[nodeId];
    let elemStr = node.elem.innerHTML;
    let width = parseInt(elemStr.split('width="')[1].split('"')[0]);
    let height = parseInt(elemStr.split('height="')[1].split('"')[0]);
    desideredWidth = Math.max(desideredWidth, width);
    desideredHeight = Math.max(desideredHeight, height);
    console.error(nodeId + " " + width + " " + height)
  }

  g = renderGraph(
    iterativelyReachedNodes,
    nodes,
    edgesDict,
    nodesep,
    edgesep,
    ranksep,
    targetDivDagre,
    desideredWidth * 2.2,
    desideredHeight * 0.7
  );

  for (let nodeId in g._nodes) {
    let node = g._nodes[nodeId];
    let elemStr = node.elem.innerHTML;
    let width = parseInt(elemStr.split('width="')[1].split('"')[0]);
    let height = parseInt(elemStr.split('height="')[1].split('"')[0]);
    graphical[graphicalDict[nodeId]].bounds.x = node.x - width / 2.0;
    graphical[graphicalDict[nodeId]].bounds.y = node.y - height / 2.0;
    graphical[graphicalDict[nodeId]].bounds.height = height;
    graphical[graphicalDict[nodeId]].bounds.width = width;
  }

  for (let edgeId in g._edgeLabels) {
    let graphEdgeObj = g._edgeObjs[edgeId];
    graphEdgeObj = graphEdgeObj.v + "@" + graphEdgeObj.w;
    let graphEdge = g._edgeLabels[edgeId];
    let edge = g._edgeLabels[edgeId];
    let graphicalElement = graphical[graphicalDict[edgesDict[graphEdgeObj]]];
    let referenceWaypoint = graphicalElement.waypoint[0];
    graphicalElement.waypoint = [];
    for (let p of edge.points) {
      let waypoint = new CustomWaypoint();
      waypoint.$type = referenceWaypoint.$type;
      waypoint.x = p.x;
      waypoint.y = p.y;
      waypoint.$parent = referenceWaypoint.$parent;
      waypoint.$attrs = referenceWaypoint.$attrs;
      waypoint.$descriptor = referenceWaypoint.$descriptor;
      waypoint.$model = referenceWaypoint.$model;
      waypoint.set("x", p.x);
      waypoint.set("y", p.y);
      graphicalElement.waypoint.push(waypoint);
    }
  }

  let xmlContent = await bpmnViewer.saveXML();

  return xmlContent.xml;
}
