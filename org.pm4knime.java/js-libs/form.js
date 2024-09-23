let tb_flag = 0;

(jsgraphviz = function() {

	let _representation;
	let _value;
	let _paper;

	let view = {}; 
	
	view.init = function(representation, value) {
		_representation = representation;
		_value = value;
		// console.error('representation.dotstr: ', representation.dotstr);

		let jsonDataFromJava = JSON.parse(representation.json);
		let nodes = jsonDataFromJava.nodes;
		let edges = jsonDataFromJava.links;
		let xml = jsonDataFromJava.xml;

		if (xml) {
			let xmlString = xml[0];
			createBpmn(xmlString);
		} else {
			 createGraphElements();
			 _paper = createPaper(nodes, edges);
		}
	};


	view.getComponentValue = () => {
		_value.dot = document.getElementById("dot").value;

		return _value;
	};

	view.getSVG = () => {
		if (_paper) {
			return createSVG(_paper);
    	} else {
	        // not supported for BPMN as we don't have a BPMN to image node
        	return null;  
	    }
	    
		
	};

	


	return view;
}());