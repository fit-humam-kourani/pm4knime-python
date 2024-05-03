(jsgraphviz = function() {

    let _representation;
    let _value;
    let _svg;

    let view = {};

    view.init = function(representation, value) {
        _representation = representation;
        _value = value;
		// console.error('representation.dotstr: ', representation.dotstr);
		// console.error('representation.parseddot: ', representation.parseddot);

        // vis = visu(representation.dotstr);
        let jsonDataFromJava = JSON.parse(representation.parseddot); 
		let nodes = jsonDataFromJava.nodes;
		let edges = jsonDataFromJava.links;
        createGraphElements(nodes, edges);
        createPaper(nodes, edges);
        
    };
    
    function createGraphElements(nodesData, edgesData) {
        
      const mainContainer = document.createElement("div");
	  mainContainer.style.display = "flex";
	  mainContainer.style.flexDirection = "column"; // Stack children vertically
	  mainContainer.style.alignItems = "center"; // Center-align children horizontally
	  mainContainer.style.justifyContent = "center";
	  // mainContainer.style.background = "#f0ede6";
	  mainContainer.style.flexDirection = "column";
	  mainContainer.style.border = "1px solid #ccc";
	  mainContainer.style.width = "1210px"; // Full width of the viewport
	  // mainContainer.style.height = "100vh"; // Full height of the viewport
	
	  const controlBar = document.createElement("div");
	  controlBar.style.background = "#e0e0e0";
	  controlBar.style.color = "#fff";
	  controlBar.style.padding = "5px";
	  controlBar.style.width = "1200px"; // Fixed width
	  // controlBar.style.height = "100px";
	  // controlBar.textContent = "Control Bar";
	  controlBar.style.fontFamily = "Arial, sans-serif";
	
	  // Create the graph container with fixed size
	  const graphContainer = document.createElement("div");
	  graphContainer.style.width = "100%"; // Fixed width
	  graphContainer.style.height = "800px"; // Fixed height
	  // graphContainer.style.border = "1px solid #ccc";
	  // graphContainer.style.padding = "10px";
	  mainContainer.style.background = "white";
	  graphContainer.style.overflow = "auto"; // Enables scrollbars if content overflows
	  graphContainer.style.margin = "auto";
	
	  const paperDiv = document.createElement("div");
	  paperDiv.id = "paper";
	  paperDiv.style.width = "100%"; // Fixed width
	  paperDiv.style.height = "100%";
	
	  // Create zoom controls div
	  const zoomControlsDiv = document.createElement("div");
	  zoomControlsDiv.id = "zoom-controls";
	
	  // Create zoom in and zoom out buttons
	  const zoomInButton = document.createElement("button");
	  zoomInButton.className = "zoom-button";
	  zoomInButton.id = "zoom-in";
	  zoomInButton.textContent = "Zoom In";
	
	  const zoomOutButton = document.createElement("button");
	  zoomOutButton.className = "zoom-button";
	  zoomOutButton.id = "zoom-out";
	  zoomOutButton.textContent = "Zoom Out";
	
	  zoomInButton.textContent = "+";
	  zoomOutButton.textContent = "-";
	
	  // // Apply CSS styles for cool shapes
	  // zoomInButton.style.fontSize = "24px"; // Large text size makes the "+" look more like a shape
	  // zoomInButton.style.width = "50px"; // Square shape
	  // zoomInButton.style.height = "50px";
	  // zoomInButton.style.lineHeight = "50px"; // Center "+" vertically
	  // zoomInButton.style.textAlign = "center"; // Center "+" horizontally
	  // zoomInButton.style.borderRadius = "50%"; // Circular shape
	  // zoomInButton.style.border = "2px solid #000"; // Solid border
	  // zoomInButton.style.display = "inline-block";
	  // zoomInButton.style.backgroundColor = "#fff"; // Background color
	  // zoomInButton.style.cursor = "pointer"; // Change cursor on hover
	
	  // // Apply the same styles to zoomOutButton, with adjustments as necessary
	  // zoomOutButton.style = zoomInButton.style.cssText; // Copy styles from zoomInButton
	  // zoomOutButton.textContent = "-";
	
	  zoomControlsDiv.appendChild(zoomInButton);
	  zoomControlsDiv.appendChild(zoomOutButton);
	
	  // document.body.appendChild(zoomControlsDiv);
	  // document.body.appendChild(paperDiv);
	
	  controlBar.appendChild(zoomControlsDiv);
	
	  graphContainer.appendChild(paperDiv);
	
	  mainContainer.appendChild(controlBar);
	  mainContainer.appendChild(graphContainer);
	  // Append the main container to the body of the document
	  document.body.appendChild(mainContainer);  
        
    };
    
    
    function estimateTextWidth(text, fontSize) {
	  if (typeof text === "undefined" || text === "") {
	    return 0; // Return a default width or 0 if there's no text to measure
	  }
	  const averageCharWidth = fontSize * 0.7; // Adjust based on your font
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
	
	// This function will check each set of three consecutive points to determine if they are collinear. If they are, the middle point can be removed. First and last points are always skipped.
	function simplifyWaypoints(points) {
	  let simplified = [];
	
	  for (let i = 1; i < points.length - 1; i++) {
	    let prev = points[i - 1];
	    let curr = points[i];
	    let next = points[i + 1];
	
	    // Calculate the determinant of the matrix formed by three points
	    // | 1  x1  y1 |
	    // | 1  x2  y2 |  = 0 for collinear points
	    // | 1  x3  y3 |
	    // If determinant is zero, points are collinear
	
	    let det =
	      prev.x * (curr.y - next.y) +
	      curr.x * (next.y - prev.y) +
	      next.x * (prev.y - curr.y);
	
	    if (Math.abs(det) > 1e-10) {
	      // Use a small epsilon to handle floating point errors
	      simplified.push(curr); // Keep current point if it's not collinear
	    }
	  }
	
	  return simplified;
	}
	

	
	function createPaper(nodes, edges) {

		var graph = new joint.dia.Graph();
		
		var paper = new joint.dia.Paper({
		  el: document.getElementById("paper"),
		  width: "100%", // Fixed width
		  height: "100%",
		  // gridSize: 10,
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
		
		    node.width = 50;
		    node.height = 50;
		
		    element = new pn.Place({
		      position: node.position,
		      attrs: attrs,
		      tokens: tokens,
		      size: { width: 50, height: 50 },
		    });
		  } else if (node.type === "transition") {
		    const fontSize = 22; // Example font size
		    const textWidth = estimateTextWidth(node.label, fontSize);
		    const transitionWidth = Math.max(textWidth + 10, 20);
		    node.width = transitionWidth;
		    node.height = 50;
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
		    // router: { name: "manhattan" },
		    // connector: { name: "smooth" },
		  });

		  graph.addCell(link);
		});
		applyAutoLayout(nodes, edges, elements);
		
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
		
		  paper.on("element:pointerup link:pointerup", (cellView) => {
		    paper.fitToContent({
		      useModelGeometry: true,
		      padding: 100 * zoomLevel,
		      allowNewOrigin: "any",
		    });
		  });
		};
		
		addZoomListeners(paper);
		
		function applyAutoLayout() {
		  
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
		    // const fontSize = 22;
		    // const textWidth = estimateTextWidth(node.label, fontSize);
		    // const transitionWidth = Math.max(textWidth + 10, 20);
		    g.setNode(node.id, {
		      width: node.width,
		      height: node.height,
		      label: node.label,
		    });
		  });
		
		  edges.forEach((edge) => {
		    g.setEdge(edge.source, edge.target, {
		      label: edge.label,
		      width: 0,
		      height: 0,
		    });
		  });
		
		  dagre.layout(g);
		
		  g.nodes().forEach(function (v) {
		    let node = g.node(v);
		    elements[v].position(node.x - node.width / 2, node.y - node.height / 2);
		    elements[v].resize(node.width, node.height);
	
		  });
		
		  g.edges().forEach(function (e) {
		    let edge = g.edge(e);
		    let sourceElement = elements[e.v];
		    let targetElement = elements[e.w];
		
		    if (sourceElement && targetElement) {
		      let links = graph
		        .getConnectedLinks(sourceElement, { outbound: true })
		        .filter((link) => link.getTargetElement() === targetElement);
		
		      if (links.length > 0) {
		        let link = links[0];
		        let waypoints = edge.points.map((point) => ({
		          x: point.x,
		          y: point.y,
		        }));
		        let simplifiedWaypoints = simplifyWaypoints(waypoints); // Simplify waypoints
		        link.vertices(simplifiedWaypoints); // Update the vertices with the simplified waypoints
		      } else {
		        console.error("No link found between nodes", e.v, "and", e.w);
		      }
		    } else {
		      console.error(
		        "Source or target elements not found for nodes",
		        e.v,
		        "and",
		        e.w
		      );
		    }
		  });
		}
		
	};

    view.getComponentValue = () => {
        _value.dot = document.getElementById("dot").value;

        return _value;
    };
    
    view.getSVG = () => {
        return (new XMLSerializer()).serializeToString(_svg);;
    };
    
    function addScript( src ) {
      var s = document.createElement( 'script' );
      s.setAttribute( 'src', src );
      document.body.appendChild( s );
    }
  
    function visu( src ) {

        //addScript( '/Users/Ralf/Documents/Git/knime/pm4knime-core/pm4knime-core/plugin/js-lib/viz/full.render.js')
        //addScript( '/Users/Ralf/Documents/Git/knime/pm4knime-core/pm4knime-core/plugin/js-lib/viz/viz.js' )



        var viz = new Viz();

        viz.renderSVGElement(src)
          .then(function(element) { 
               
           _svg = element;
           
           var exportA = document.createElement('a');
           exportA.innerHTML = `<button type="button">Export</button>`;
           exportA.href = viz.generate_url(element);
           exportA.download = "js-view.svg";
           
           

           var zoomIn = document.createElement('a');
           zoomIn.innerHTML = `<button type="button">Zoom in</button>`;
           zoomIn.addEventListener( 'click', function(){
               element.setAttribute("max-height", "none"); 
               element.setAttribute("max-width", "none"); 
               var width = element.clientWidth*1.1; 
               var height = element.clientHeight*1.1; 
               element.setAttribute("width", width + "px"); 
               element.setAttribute("height", height + "px"); 
               
           });
           
           var zoomOut = document.createElement('a');
           zoomOut.innerHTML = `<button type="button">Zoom out</button>`;
           zoomOut.addEventListener( 'click', function(){
               element.setAttribute("max-height", "none"); 
               element.setAttribute("max-width", "none"); 
               var width = element.clientWidth*0.9; 
               var height = element.clientHeight*0.9; 
               element.setAttribute("width", width + "px"); 
               element.setAttribute("height", height + "px"); 
           });
           
           var parent1 = document.createElement("div");
           parent1.style.top = "0px";
           parent1.style.left = "0px";
           parent1.style.position = "fixed";
           parent1.style.border = "groove #e6e6e6";
           parent1.appendChild(exportA);
           parent1.appendChild(zoomIn);
           parent1.appendChild(zoomOut);
           document.body.appendChild(parent1);     
                     
           var parent2 = document.createElement("div");
           parent2.style.top = "50px";
           parent2.style.left = "0px";
           parent2.style.position = "absolute";
           parent2.appendChild(element);              
           document.body.appendChild(parent2); 
            
          })
          .catch(error => {
            // Create a new Viz instance (@see Caveats page for more info)
            viz = new Viz();

            // Possibly display the error
            console.error(error);
          });     
          
          

    }
    
    

    return view;
}());
