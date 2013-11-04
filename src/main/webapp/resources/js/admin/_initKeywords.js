$(function(){
	//TODO synonymZone and childrenZone have the same code. Please factorize it
	$(".orphanKeyword").draggable({
		snap: ".childrenZone",
		snapMode: "inner",
		scrollSensitivity : 100,
		scrollSpeed : 10,
		helper: "clone",
		appendTo: "body"
	});
	$("#childrenKeyword .childrenZone").droppable({
		hoverClass: "hoverDroppable",
		accept: function(e){
			return $(e).is("#orphanMainKeywords .orphanKeyword") 
						&& ! $(e).is("#orphanMainKeywords .orphanExternalKeyword") 
						&& $(this).attr("data-id") != $(e).attr("data-id");
		},
		drop: function(event, ui){
			//retrive the list of dropped keyword ids
			var ids = ""+$(".childrenIds input",this).val();
			//retrive droped keyword
			var draggedId = ui.draggable.attr("data-id");
			//add Id to the list
			ids = addToStringList(ids,draggedId);
			//refresh Ids list on the page
			$(".childrenIds input",this).val(ids);
			var cloneDragged = ui.draggable.clone();
			//redisplay the keyword dropped into the new parent keyword
			cloneDragged.removeAttr("style");
			cloneDragged.prepend($("<span />")
					.addClass("ui-icon ui-icon-arrowreturnthick-1-s")
					.addClass("revert"));
			$(this).append(cloneDragged);
			//add a fonction to remove added keyword
			$(".revert",cloneDragged).click(function(){
				var draggedDiv = $(this).parent(".orphanKeyword");
				var id = draggedDiv.attr("data-id");
				$(this).parent(".orphanKeyword").remove();
				$("#orphanMainKeywords .orphanKeyword[data-id="+id+"], #orphanExternalKeywords .orphanKeyword[data-id="+id+"]").show(400);
			});
			ui.draggable.hide();
		}
	});
	$("#childrenKeyword .synonymZone").droppable({
		hoverClass: "hoverDroppable",
		accept: function(e){
			return $(this).attr("data-id") != $(e).attr("data-id");
		},
		drop: function(event, ui){
			//retrive the list of dropped keyword ids
			var ids = ""+$(".synonymIds input",this).val();
			//retrive droped keyword
			var draggedId = ui.draggable.attr("data-id");
			//add Id to the list
			ids = addToStringList(ids,draggedId);
			//refresh Ids list on the page
			$(".synonymIds input",this).val(ids);
			var cloneDragged = ui.draggable.clone();
			//redisplay the keyword dropped into the new parent keyword
			cloneDragged.removeAttr("style");
			cloneDragged.prepend($("<span />")
					.addClass("ui-icon ui-icon-arrowreturnthick-1-s")
					.addClass("revert"));
			$(this).append(cloneDragged);
			//add a fonction to remove added keyword
			$(".revert",cloneDragged).click(function(){
				var draggedDiv = $(this).parent(".orphanKeyword");
				var id = draggedDiv.attr("data-id");
				$(this).parent(".orphanKeyword").remove();
				$("#orphanMainKeywords .orphanKeyword[data-id="+id+"], #orphanExternalKeywords .orphanKeyword[data-id="+id+"]").show(400);
			});
			ui.draggable.hide();
		}
	});
});