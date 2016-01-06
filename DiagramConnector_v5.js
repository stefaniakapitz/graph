/**
 * @author Stefania, Günay, Lukas R.
 */
//the first function ist always the same. With the path to the abstractjavascriptcomponent
window.de_unistuttgart_ims_fictionnet_gui_components_graph_Diagram = function() {
	  // return corresponding dom element for the diagram
    var diagramElement = this.getElement();
    // changes the diagram on state
    var flag =1;
    this.onStateChange = function() {
    	
        var coords = this.getState().coords;
        var value = this.getState().chartblue;
        var diagram = this.getState().diagram;
        var chapter = this.getState().chapters;
        var person = this.getState().persons;
        var chapter2 = this.getState().chapters2;
        var person2 = this.getState().persons2;
        var convObj = this.getState().convPersons;
        var group = this.getState().groups;
        var source = this.getState().source;
        var target = this.getState().target;
        var value = this.getState().value;
      
 // get parsed Data               
var data = getJSON(person,chapter);
var data2= getJSON(person2,chapter2);
var nodeData = getJSONFDGNode(group,convObj);
var nodeLink = getJSONFDGLink(source,target,value);

// switch sort of diagram
    switch(diagram){
    case "ForceDirectedGraph":
//	alert(JSON.stringify(nodeData));
var w = 700,
    h = 700;


    var vis = d3.select(diagramElement).append("svg:svg")
    .attr("width", w)
    .attr("height", h)
    .call(d3.behavior.zoom().on("zoom", onZoom))
    .append('g');
    
    function onZoom() {
  vis.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
}
               
     vis.append("svg:defs").selectAll("marker")
    .data(["end"])
  .enter().append("svg:marker")
    .attr("id", String)
    .attr("viewBox", "0 -5 10 10")
    .attr("refX", 15)
    .attr("refY", -1.5)
    .attr("markerWidth", 10)
    .attr("markerHeight", 10)
    .attr("orient", "auto")
  .append("svg:path")
    .attr("d", "M0,-5L10,0L0,5");
    
    var color = d3.scale.category20();
    
    var force = self.force = d3.layout.force()
        .nodes(nodeData)
        .links(nodeLink)
        .gravity(.05)
        .distance(100)
        .charge(-100)
        .size([w, h])
        .start();

    var link = vis.selectAll("line.link")
        .data(nodeLink)
        .enter().append("svg:line")
        .attr("class", "link")
        .attr("marker-end", "url(#end)")
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });
// d3.behavior.drag().on().on
    var node_drag = d3.behavior.drag()
        .on("dragstart", dragstart)
        .on("drag", dragmove)
        .on("dragend", dragend);

    function dragstart(d, i) {
    	d3.event.sourceEvent.stopPropagation();
        //force.stop() // stops the force auto positioning before you start dragging
    }

    function dragmove(d, i) {
        d.px += d3.event.dx;
        d.py += d3.event.dy;
        d.x += d3.event.dx;
        d.y += d3.event.dy; 
        tick(); // this is the key to make it work together with updating both px,py,x,y on d !
    }

    function dragend(d, i) {
        d.fixed = true; // of course set the node to fixed so the force doesn't include the node in its auto positioning stuff
        tick();
        force.resume();
    }

    var node = vis.selectAll("g.node")
        .data(nodeData)
      .enter().append("svg:g")
        .attr("class", "node")
        .call(node_drag);

   node.append("circle")
        .attr("class", "node")
        .attr("r", 5)
        .style("fill", function(d) { return color(d.group); });
       

    node.append("svg:text")
        .attr("class", "nodetext")
        .attr("dx", 12)
        .attr("dy", ".35em")
        .text(function(d) { return d.name });

    force.on("tick", tick);

    function tick() {
      link.attr("x1", function(d) { return d.source.x; })
          .attr("y1", function(d) { return d.source.y; })
          .attr("x2", function(d) { return d.target.x; })
          .attr("y2", function(d) { return d.target.y; });

      node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
    };


    
    	break;    	
    case"BarChart":
    	//alert(JSON.stringify(nodeData));
    	//alert(JSON.stringify(nodeLink));
   
    var margin = {top: 20, right: 20, bottom: 70, left: 40},
    width = 600 - margin.left - margin.right,
    height = 300 - margin.top - margin.bottom;

   var x = d3.scale.ordinal().rangeRoundBands([0, width], .05);

var y = d3.scale.linear()
    .range([height, 0]);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom")
    .innerTickSize(-height)
    .outerTickSize(0)
    .tickPadding(10);    

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .innerTickSize(-width)
    .outerTickSize(0)
    .tickPadding(10);

var line = d3.svg.line()
    .x(function(d) { return x(d.chapter); })
    .y(function(d) { return y(d.person); });

var svg = d3.select(diagramElement).append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  data.forEach(function(d) {
    d.chapter = +d.chapter;
    d.person = +d.person;
  });

   x.domain(data.map(function(d) { return d.chapter; }));
   y.domain([0, d3.max(data, function(d) { return d.person; })]);  

  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis)
      .append("text")
      .attr("transform", "translate(" + (width / 2) + " ," + ( margin.bottom-35) +")")
    .style("text-anchor", "middle")
    .text("chapter");

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("Occurence");
            
            svg.selectAll("bar")
      .data(data)
    .enter().append("rect")
      .style("fill", "steelblue")
      .attr("x", function(d) { return x(d.chapter); })
      .attr("width", x.rangeBand())
      .attr("y", function(d) { return y(d.person); })
      .attr("height", function(d) { return height - y(d.person); });
          
//            svg.on("click", function(){
//            	 alert(d3.select(diagramElement).html());
//    });

break;
    case "LineChart":
	var margin = {top: 20, right: 20, bottom: 70, left: 40},
    width = 600 - margin.left - margin.right,
    height = 300 - margin.top - margin.bottom;

   var x = d3.scale.ordinal().rangeRoundBands([0, width], .05);

var y = d3.scale.linear()
    .range([height, 0]);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom")
    .innerTickSize(-height)
    .outerTickSize(0)
    .tickPadding(10);    

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .innerTickSize(-width)
    .outerTickSize(0)
    .tickPadding(10);

var line = d3.svg.line()
    .x(function(d) { return x(d.chapter); })
    .y(function(d) { return y(d.person); });

var svg = d3.select(diagramElement).append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  data.forEach(function(d) {
    d.chapter = +d.chapter;
    d.person = +d.person;
  });

   x.domain(data.map(function(d) { return d.chapter; }));
   y.domain([0, d3.max(data, function(d) { return d.person; })]);  

  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis)
      .append("text")
       .attr("transform", "translate(" + (width / 2) + " ," + ( margin.bottom-35) +")")
       .style("text-anchor", "middle")
       .text("chapter");

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("Occurence");

  svg.append("path")
      .datum(data)
      .attr("class", "line")
      .attr("d", line);
      // 2te Linie
   //svg.append("path").datum(data2).attr("class","line").attr("d",line);
                
    	break;
    	default:  
    }
}

// data parser for bar and line chart       
function getJSON(arrayID,arrayText) {    
    var JSON = "[";
    //should arrayID length equal arrayText lenght and both against null
    if (arrayID != null && arrayText != null && arrayID.length == arrayText.length) {
        for (var i = 0; i < arrayID.length; i++) {
            JSON += "{";
            JSON += "chapter:" + arrayText[i] + ",";
            JSON += "person:" + arrayID[i];
            JSON += "},";
        }
    }
    JSON += "]"
    JSON = Function("return " + JSON + " ;");
    return JSON();
}  

// Act indentifies groups, shown as color of the node
//Name is identifier
 function getJSONFDGNode(arrayID,arrayText) {    
    var JSON = "[";
    //should arrayID length equal arrayText lenght and both against null
    if (arrayID != null && arrayText != null && arrayID.length == arrayText.length) {
        for (var i = 0; i < arrayID.length; i++) {
            JSON += "{";
            JSON += "name:" + "'"+arrayText[i] +"'"+ ",";
            JSON += "group:"+arrayID[i];
            JSON += "},";
        }
    }
    JSON += "]"
    JSON = Function("return " + JSON + " ;");
    return JSON();
}  
//link: value indentfies how often the persons appear in a scene

 function getJSONFDGLink(sourceArray,targetArray,valueArray) {    
    var JSON = "[";
    //should arrayID length equal arrayText lenght and both against null
    if (sourceArray != null && targetArray != null && valueArray !=null && sourceArray.length == targetArray.length) {
        for (var i = 0; i < sourceArray.length; i++) {
            JSON += "{";
            JSON += "source:" + sourceArray[i] + ",";
            JSON += "target:" + targetArray[i] + ",";
            JSON += "value:" + valueArray[i] + ",";
            JSON += "},";
        }
    }
    JSON += "]"
    JSON = Function("return " + JSON + " ;");
    return JSON();
}

    var saveAs = saveAs || (function(view) {
            "use strict";
            // IE <10 is explicitly unsupported
            if (typeof navigator !== "undefined" && /MSIE [1-9]\./.test(navigator.userAgent)) {
                return;
            }
            var
                doc = view.document
            // only get URL when necessary in case Blob.js hasn't overridden it yet
                , get_URL = function() {
                    return view.URL || view.webkitURL || view;
                }
                , save_link = doc.createElementNS("http://www.w3.org/1999/xhtml", "a")
                , can_use_save_link = "download" in save_link
                , click = function(node) {
                    var event = new MouseEvent("click");
                    node.dispatchEvent(event);
                }
                , is_safari = /Version\/[\d\.]+.*Safari/.test(navigator.userAgent)
                , webkit_req_fs = view.webkitRequestFileSystem
                , req_fs = view.requestFileSystem || webkit_req_fs || view.mozRequestFileSystem
                , throw_outside = function(ex) {
                    (view.setImmediate || view.setTimeout)(function() {
                        throw ex;
                    }, 0);
                }
                , force_saveable_type = "application/octet-stream"
                , fs_min_size = 0
            // See https://code.google.com/p/chromium/issues/detail?id=375297#c7 and
            // https://github.com/eligrey/FileSaver.js/commit/485930a#commitcomment-8768047
            // for the reasoning behind the timeout and revocation flow
                , arbitrary_revoke_timeout = 500 // in ms
                , revoke = function(file) {
                    var revoker = function() {
                        if (typeof file === "string") { // file is an object URL
                            get_URL().revokeObjectURL(file);
                        } else { // file is a File
                            file.remove();
                        }
                    };
                    if (view.chrome) {
                        revoker();
                    } else {
                        setTimeout(revoker, arbitrary_revoke_timeout);
                    }
                }
                , dispatch = function(filesaver, event_types, event) {
                    event_types = [].concat(event_types);
                    var i = event_types.length;
                    while (i--) {
                        var listener = filesaver["on" + event_types[i]];
                        if (typeof listener === "function") {
                            try {
                                listener.call(filesaver, event || filesaver);
                            } catch (ex) {
                                throw_outside(ex);
                            }
                        }
                    }
                }
                , auto_bom = function(blob) {
                    // prepend BOM for UTF-8 XML and text/* types (including HTML)
                    if (/^\s*(?:text\/\S*|application\/xml|\S*\/\S*\+xml)\s*;.*charset\s*=\s*utf-8/i.test(blob.type)) {
                        return new Blob(["\ufeff", blob], {type: blob.type});
                    }
                    return blob;
                }
                , FileSaver = function(blob, name, no_auto_bom) {
                    if (!no_auto_bom) {
                        blob = auto_bom(blob);
                    }
                    // First try a.download, then web filesystem, then object URLs
                    var
                        filesaver = this
                        , type = blob.type
                        , blob_changed = false
                        , object_url
                        , target_view
                        , dispatch_all = function() {
                            dispatch(filesaver, "writestart progress write writeend".split(" "));
                        }
                    // on any filesys errors revert to saving with object URLs
                        , fs_error = function() {
                            if (target_view && is_safari && typeof FileReader !== "undefined") {
                                // Safari doesn't allow downloading of blob urls
                                var reader = new FileReader();
                                reader.onloadend = function() {
                                    var base64Data = reader.result;
                                    target_view.location.href = "data:attachment/file" + base64Data.slice(base64Data.search(/[,;]/));
                                    filesaver.readyState = filesaver.DONE;
                                    dispatch_all();
                                };
                                reader.readAsDataURL(blob);
                                filesaver.readyState = filesaver.INIT;
                                return;
                            }
                            // don't create more object URLs than needed
                            if (blob_changed || !object_url) {
                                object_url = get_URL().createObjectURL(blob);
                            }
                            if (target_view) {
                                target_view.location.href = object_url;
                            } else {
                                var new_tab = view.open(object_url, "_blank");
                                if (new_tab == undefined && is_safari) {
                                    //Apple do not allow window.open, see http://bit.ly/1kZffRI
                                    view.location.href = object_url
                                }
                            }
                            filesaver.readyState = filesaver.DONE;
                            dispatch_all();
                            revoke(object_url);
                        }
                        , abortable = function(func) {
                            return function() {
                                if (filesaver.readyState !== filesaver.DONE) {
                                    return func.apply(this, arguments);
                                }
                            };
                        }
                        , create_if_not_found = {create: true, exclusive: false}
                        , slice
                        ;
                    filesaver.readyState = filesaver.INIT;
                    if (!name) {
                        name = "download";
                    }
                    if (can_use_save_link) {
                        object_url = get_URL().createObjectURL(blob);
                        setTimeout(function() {
                            save_link.href = object_url;
                            save_link.download = name;
                            click(save_link);
                            dispatch_all();
                            revoke(object_url);
                            filesaver.readyState = filesaver.DONE;
                        });
                        return;
                    }
                    // Object and web filesystem URLs have a problem saving in Google Chrome when
                    // viewed in a tab, so I force save with application/octet-stream
                    // http://code.google.com/p/chromium/issues/detail?id=91158
                    // Update: Google errantly closed 91158, I submitted it again:
                    // https://code.google.com/p/chromium/issues/detail?id=389642
                    if (view.chrome && type && type !== force_saveable_type) {
                        slice = blob.slice || blob.webkitSlice;
                        blob = slice.call(blob, 0, blob.size, force_saveable_type);
                        blob_changed = true;
                    }
                    // Since I can't be sure that the guessed media type will trigger a download
                    // in WebKit, I append .download to the filename.
                    // https://bugs.webkit.org/show_bug.cgi?id=65440
                    if (webkit_req_fs && name !== "download") {
                        name += ".download";
                    }
                    if (type === force_saveable_type || webkit_req_fs) {
                        target_view = view;
                    }
                    if (!req_fs) {
                        fs_error();
                        return;
                    }
                    fs_min_size += blob.size;
                    req_fs(view.TEMPORARY, fs_min_size, abortable(function(fs) {
                        fs.root.getDirectory("saved", create_if_not_found, abortable(function(dir) {
                            var save = function() {
                                dir.getFile(name, create_if_not_found, abortable(function(file) {
                                    file.createWriter(abortable(function(writer) {
                                        writer.onwriteend = function(event) {
                                            target_view.location.href = file.toURL();
                                            filesaver.readyState = filesaver.DONE;
                                            dispatch(filesaver, "writeend", event);
                                            revoke(file);
                                        };
                                        writer.onerror = function() {
                                            var error = writer.error;
                                            if (error.code !== error.ABORT_ERR) {
                                                fs_error();
                                            }
                                        };
                                        "writestart progress write abort".split(" ").forEach(function(event) {
                                            writer["on" + event] = filesaver["on" + event];
                                        });
                                        writer.write(blob);
                                        filesaver.abort = function() {
                                            writer.abort();
                                            filesaver.readyState = filesaver.DONE;
                                        };
                                        filesaver.readyState = filesaver.WRITING;
                                    }), fs_error);
                                }), fs_error);
                            };
                            dir.getFile(name, {create: false}, abortable(function(file) {
                                // delete file if it already exists
                                file.remove();
                                save();
                            }), abortable(function(ex) {
                                if (ex.code === ex.NOT_FOUND_ERR) {
                                    save();
                                } else {
                                    fs_error();
                                }
                            }));
                        }), fs_error);
                    }), fs_error);
                }
                , FS_proto = FileSaver.prototype
                , saveAs = function(blob, name, no_auto_bom) {
                    return new FileSaver(blob, name, no_auto_bom);
                }
                ;
            // IE 10+ (native saveAs)
            if (typeof navigator !== "undefined" && navigator.msSaveOrOpenBlob) {
                return function(blob, name, no_auto_bom) {
                    if (!no_auto_bom) {
                        blob = auto_bom(blob);
                    }
                    return navigator.msSaveOrOpenBlob(blob, name || "download");
                };
            }

            FS_proto.abort = function() {
                var filesaver = this;
                filesaver.readyState = filesaver.DONE;
                dispatch(filesaver, "abort");
            };
            FS_proto.readyState = FS_proto.INIT = 0;
            FS_proto.WRITING = 1;
            FS_proto.DONE = 2;

            FS_proto.error =
                FS_proto.onwritestart =
                    FS_proto.onprogress =
                        FS_proto.onwrite =
                            FS_proto.onabort =
                                FS_proto.onerror =
                                    FS_proto.onwriteend =
                                        null;

            return saveAs;
        }(
            typeof self !== "undefined" && self
            || typeof window !== "undefined" && window
            || this.content
        ));
    // `self` is undefined in Firefox for Android content script context
    // while `this` is nsIContentFrameMessageManager
    // with an attribute `content` that corresponds to the window

    if (typeof module !== "undefined" && module.exports) {
        module.exports.saveAs = saveAs;
    } else if ((typeof define !== "undefined" && define !== null) && (define.amd != null)) {
        define([], function() {
            return saveAs;
        });
    }

    function data2blob(data,isBase64)
    {
        var chars="";
        if (isBase64) chars=atob(data); else chars=data;
        var bytes=new Array(chars.length);
        for (var i=0;i<chars.length; i++) bytes[i]=chars.charCodeAt(i);
        var blob=new Blob([new Uint8Array(bytes)]);
        return blob;
    }
	
	function exportGraphToGraphML(){
		var tmp  = document.getElementById("ex1");
        var svg = tmp.getElementsByTagName("svg")[0];
        svg.toXMLString()
        var svgData = new XMLSerializer().serializeToString(svg);

        saveAs( data2blob(svgData), "graph.XML" );
        // return svgData -> fileDialog
	}

    function exportGraphToPNG() {
    	  var svg = d3.select(diagramElement).append("svg")
          .attr("width", width)
          .attr("height", height);

      var canvas = document.createElement("canvas");
      var svgData = new XMLSerializer().serializeToString(svg);
      var pos = svgData.indexOf("<defs>") + 6;


      var css = "<style type='text/css'><![CDATA[ svg{font:10px sans-serif;-webkit-tap-highlight-color:transparent} line, path{fill:none;stroke:#000} text{-webkit-user-select:none;-moz-user-select:none;user-select:none}.c3-bars path,.c3-event-rect,.c3-legend-item-tile,.c3-xgrid-focus,.c3-ygrid{shape-rendering:crispEdges}.c3-chart-arc path{stroke:#fff}.c3-chart-arc text{fill:#fff;font-size:13px}.c3-grid line{stroke:#aaa}.c3-grid text{fill:#aaa}.c3-xgrid,.c3-ygrid{stroke-dasharray:3 3}.c3-text.c3-empty{fill:gray;font-size:2em}.c3-line{stroke-width:1px}.c3-circle._expanded_{stroke-width:1px;stroke:#fff}.c3-selected-circle{fill:#fff;stroke-width:2px}.c3-bar{stroke-width:0}.c3-bar._expanded_{fill-opacity:.75}.c3-target.c3-focused{opacity:1}.c3-target.c3-focused path.c3-line,.c3-target.c3-focused path.c3-step{stroke-width:2px}.c3-target.c3-defocused{opacity:.3!important}.c3-region{fill:#4682b4;fill-opacity:.1}.c3-brush .extent{fill-opacity:.1}.c3-legend-item{font-size:12px}.c3-legend-item-hidden{opacity:.15}.c3-legend-background{opacity:.75;fill:#fff;stroke:#d3d3d3;stroke-width:1}.c3-title{font:14px sans-serif}.c3-tooltip-container{z-index:10}.c3-tooltip{border-collapse:collapse;border-spacing:0;background-color:#fff;empty-cells:show;-webkit-box-shadow:7px 7px 12px -9px #777;-moz-box-shadow:7px 7px 12px -9px #777;box-shadow:7px 7px 12px -9px #777;opacity:.9}.c3-tooltip tr{border:1px solid #CCC}.c3-tooltip th{background-color:#aaa;font-size:14px;padding:2px 5px;text-align:left;color:#FFF}.c3-tooltip td{font-size:13px;padding:3px 6px;background-color:#fff;border-left:1px dotted #999}.c3-tooltip td>span{display:inline-block;width:10px;height:10px;margin-right:6px}.c3-tooltip td.value{text-align:right}.c3-area{stroke-width:0;opacity:.2}.c3-chart-arcs-title{dominant-baseline:middle;font-size:1.3em}.c3-chart-arcs .c3-chart-arcs-background{fill:#e0e0e0;stroke:none}.c3-chart-arcs .c3-chart-arcs-gauge-unit{fill:#000;font-size:16px}.c3-chart-arcs .c3-chart-arcs-gauge-max,.c3-chart-arcs .c3-chart-arcs-gauge-min{fill:#777}.c3-chart-arc .c3-gauge-value{fill:#000}]]></style> "


      var output = [ svgData.slice(0, pos), css, svgData.slice(pos) ].join('');

      var svgSize = svg.getBoundingClientRect();
      canvas.width = svgSize.width + 100;
      canvas.height = svgSize.height + 100;
      var ctx = canvas.getContext("2d");

      var img = document.createElement("img");
      img.setAttribute("src", "data:image/svg+xml;base64," + btoa(output));

      img.onload = function() {
          ctx.drawImage(img, 0, 0);
      }

      var image;

      image = Canvas2Image.convertToImage(img, svgSize.width,
          svgSize.height, "png")

      saveAs( data2blob(image), "graph.PNG" );
        // return image -> file Dialog
    }
    
    
    
}