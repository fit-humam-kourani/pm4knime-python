function createAndManageGraph(nodes, edges) {

	const createGraphElements = () => {
	  const paperDiv = document.createElement("div");
	  paperDiv.id = "paper";
	
	  // Create zoom controls div
	  const zoomControlsDiv = document.createElement("div");
	  zoomControlsDiv.id = "zoom-controls";
	
	  // Create zoom in and zoom out buttons
	  const zoomInButton = document.createElement("button");
	  zoomInButton.id = "zoom-in";
	  zoomInButton.textContent = "Zoom In";
	
	  const zoomOutButton = document.createElement("button");
	  zoomOutButton.id = "zoom-out";
	  zoomOutButton.textContent = "Zoom Out";
	
	  zoomControlsDiv.appendChild(zoomInButton);
	  zoomControlsDiv.appendChild(zoomOutButton);
	
	  document.body.appendChild(zoomControlsDiv);
	  document.body.appendChild(paperDiv);
	};
	
	createGraphElements();
	
	function estimateTextWidth(text, fontSize) {
	  if (typeof text === "undefined" || text === "") {
	    return 0; // Return a default width or 0 if there's no text to measure
	  }
	  const averageCharWidth = fontSize * 0.5; // Adjust based on your font
	  return text.length * averageCharWidth;
	}
	
	function adjustPaperSize(graph, paper) {
	  let maxX = 0;
	  let maxY = 0;
	
	  graph.getElements().forEach((element) => {
	    let position = element.position();
	    let size = element.size();
	    maxX = Math.max(maxX, position.x + size.width);
	    maxY = Math.max(maxY, position.y + size.height);
	  });
	
	  paper.setDimensions(maxX + 100, maxY + 100);
	
	  // let bbox = V(paper.viewport).bbox(true);
	  // paper.scaleContentToFit({ padding: 10, bbox: bbox });
	  // paper.centerContent();
	}
	
	function applyAutoLayout(nodes, edges) {
	  var g = new dagre.graphlib.Graph();
	
	  g.setGraph({
	    rankdir: "LR",
	    marginx: 20,
	    marginy: 20,
	  });
	
	  g.setDefaultEdgeLabel(function () {
	    return {};
	  });
	
	  nodes.forEach((node) => {
	    g.setNode(node.id, { label: node.type, width: 100, height: 50 });
	  });
	
	  edges.forEach((edge) => {
	    g.setEdge(edge.source, edge.target);
	  });
	
	  dagre.layout(g);
	
	  g.nodes().forEach(function (v) {
	    let node = g.node(v);
	    elements[v].position(node.x, node.y);
	  });
	}
	
	var graph = new joint.dia.Graph();
	
	var paper = new joint.dia.Paper({
	  el: document.getElementById("paper"),
	  width: 800,
	  height: 350,
	  gridSize: 10,
	  defaultAnchor: { name: "perpendicular" },
	  defaultConnectionPoint: { name: "boundary" },
	  model: graph,
	});
	
	var pn = joint.shapes.pn;
	
	var elements = {};
	
	// Add nodes
	nodes.forEach(function (node) {
	  var element;
	  if (node.type === "place") {
	    let attrs = {
	      ".root": { stroke: "#6aa84f", "stroke-width": 2 },
	      ".tokens > circle": { fill: "#38761d" },
	    };
	    let tokens = 0;
	
	    if (node.f_marking === true) {
	      attrs[".root"]["stroke-width"] = 4;
	    }
	    if (node.i_marking === true) {
	      tokens = 1;
	    }
	
	    element = new pn.Place({
	      position: node.position,
	      attrs: attrs,
	      tokens: tokens,
	    });
	  } else if (node.type === "transition") {
	    const fontSize = 22; // Example font size
	    const textWidth = estimateTextWidth(node.label, fontSize);
	    const transitionWidth = Math.max(textWidth + 10, 20);
	    element = new pn.Transition({
	      position: node.position,
	      size: { width: transitionWidth, height: 50 },
	      attrs: {
	        ".label": {
	          text: node.label || "",
	          fill: "black",
	          "font-size": fontSize,
	          "ref-x": 0.5,
	          "ref-y": 0.5,
	          "text-anchor": "middle",
	          "y-alignment": "middle",
	        },
	        ".root": {
	          fill: "#cfe2f3",
	          stroke: "#3f77cf",
	          "stroke-width": 2,
	        },
	      },
	    });
	  }
	  graph.addCell(element);
	  elements[node.id] = element;
	});
	
	edges.forEach(function (edge) {
	  var link = new pn.Link({
	    source: { id: elements[edge.source].id, selector: ".root" },
	    target: { id: elements[edge.target].id, selector: ".root" },
	    attrs: {
	      ".connection": { stroke: "grey", "stroke-width": 3 },
	      ".marker-target": { fill: "grey", stroke: "grey", "stroke-width": 2 },
	    },
	  });
	  graph.addCell(link);
	});
	applyAutoLayout(nodes, edges);
	
	adjustPaperSize(graph, paper);
	
	const addZoomListeners = (paper) => {
	  let zoomLevel = 1;
	
	  const zoom = (zoomLevel) => {
	    paper.scale(zoomLevel);
	    paper.fitToContent({
	      useModelGeometry: true,
	      padding: 100 * zoomLevel,
	      allowNewOrigin: "any",
	    });
	  };
	
	  document.getElementById("zoom-in").addEventListener("click", () => {
	    zoomLevel = Math.min(3, zoomLevel + 0.2);
	    zoom(zoomLevel);
	  });
	
	  document.getElementById("zoom-out").addEventListener("click", () => {
	    zoomLevel = Math.max(0.2, zoomLevel - 0.2);
	    zoom(zoomLevel);
	  });
	
	  // Mouse wheel listener for zooming
	  paper.el.addEventListener("wheel", (event) => {
	    event.preventDefault(); // Prevent scrolling
	    const delta = event.deltaY;
	    // Determine zoom direction
	    if (delta > 0) {
	      // Zoom out
	      zoomLevel = Math.max(0.2, zoomLevel - 0.2);
	    } else if (delta < 0) {
	      // Zoom in
	      zoomLevel = Math.min(3, zoomLevel + 0.2);
	    }
	    zoom(zoomLevel);
	  });
	};
	
	addZoomListeners(paper);

}