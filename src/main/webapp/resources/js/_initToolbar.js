$(function(){
	
	$("#searchForm\\:searchCity").autocomplete({
		source: 
			function( request, response ) {
				$.getJSON(
					_requestContextPath_+"/rest/search/places",
					{
						term : request.term
					},function( data ) {
			          	response( data );
					}
				);
			},
		minLength: 2,
		select:
			function (event, ui){
				$(this).val(ui.item.name);
				return false;
			},
		create: function () {
            $(this).data("ui-autocomplete")._renderItem = function (ul, place) {
                return $("<li />")
					.attr( "data-value", place.id )
                    .append($("<a />").text(place.name + " (" + place.postalCodes + ")"))
                    .appendTo(ul);
            };
        }
    });
	$("#searchForm\\:searchMember").autocomplete({
		source: 
			function( request, response ) {
			$.getJSON(
					_requestContextPath_+"/rest/authentication/searchUser",
					{
						q : request.term
					},function( data ) {
						response( data );
					}
			);
		},
		minLength: 2,
		select:
			function (event, ui){
			$(this).val(ui.item.login);
			return false;
		},
		create: function () {
			$(this).data("ui-autocomplete")._renderItem = function (ul, user) {
				return $("<li />")
				.attr( "data-value", user.id )
				.append($("<a />").text(user.login))
				.appendTo(ul);
			};
		}
	});
    		
    var timeoutSlider = null;
    var timeoutSlider0 = null;
    var timeoutSlider1 = null;
	$( "#distanceSlider" ).slider({
	      range: "max",
	      min: 0,
	      max: 1000,
	      step : 10,
	      value: 10,
	      slide: function( event, ui ) {
	      	$("#distanceSlider").show(200);
	      	$("#distanceSlider").addClass("activated");
	      	clearTimeout(timeoutSlider);
    	 	clearTimeout(timeoutSlider0);
    	 	clearTimeout(timeoutSlider1);
    	 	if(ui.value > 100){
	      		$(this).slider("option", "step", 50);
	      	}else{
	      		$(this).slider("option", "step", 10);
	      	}
	      	if(ui.value == $(this).slider("option","max")){
    	 		$("#searchForm\\:distance").val("").hide(0);
    	 		$("#searchForm\\:labelDistance").text("toute la France");
    	 	}else{
	        	$("#searchForm\\:distance").val( ui.value ).show(0);
	        	$("#searchForm\\:labelDistance").text("km");
        	}
	        timeoutSlider1 = setTimeout(function(){
			    $("#distanceSlider").removeClass("activated");
			    $("#distanceSlider").hide(200);
	    	},500);
	      }
    }).offset({
    	left :  $("#searchForm\\:distance" ).offset().left, 
    	top : $( "#searchForm\\:distance" ).offset().top-30 
    });
    $("#distanceBox").mouseenter(function(e){
    	$("#distanceSlider").show(200);
    }).mouseleave(function(e){
    		timeoutSlider = setTimeout(function(){
		    		if(!$("#distanceSlider").hasClass("activated")){
		    			$("#distanceSlider").hide(200);
	    			 	$("#distanceSlider").removeClass("activated");
	    			}
    			}, 500);
	});
	
	$("#distanceSlider").mouseenter( function() {
    	$("#distanceSlider").show(200);
	 	clearTimeout(timeoutSlider);
	 	clearTimeout(timeoutSlider0);
	 	clearTimeout(timeoutSlider1);
	    $(this).addClass("activated");
	}).mouseleave(function(){
		timeoutSlider0 = setTimeout(function(){
		    $("#distanceSlider").removeClass("activated");
		    $("#distanceSlider").hide(200);
	    },500);
	});
		
	if(isEmpty($("#searchForm\\:distance").val())){
		$("#searchForm\\:distance").val("10");
		$("#distanceSlider").slider( "option", "value", 10 );
	}
});