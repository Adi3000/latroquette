$(function(){
	var validatePlace = null;
	$("#userPlaceName")
		.tooltip({
            tooltipClass: "ui-state-highlight"
      	}).autocomplete({
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
					$(this).val(ui.item.name + " (" + ui.item.postalCodes + ")");
					$(this).siblings('input[type="hidden"]').val(ui.item.id);
					return false;
				},
			create: 
				function () {
		            $(this).data("ui-autocomplete")._renderItem = function (ul, place) {
		                return $("<li />")
							.attr( "data-value", place.id )
		                    .append($("<a />").text(place.name + " (" + place.postalCodes + ")"))
		                    .appendTo(ul);
		            };
	       		},
	       	change: 
	       		function(event, ui){
	       			var field = $(this);
	       			clearTimeout(validatePlace);
	       			field.tooltip( "close" );
	       			if(ui.item) {
	       				field
	       					.val(ui.item.name + " (" + ui.item.postalCodes + ")")
							.siblings('input[type="hidden"]').val(ui.item.id);
	       			}else{
	       				field
	       					.attr( "title", field.val() + " n'est pas valide" )
  							.tooltip( "open" );
       					field.siblings('input[type="hidden"]').val("");
       					validatePlace = setTimeout(function() {
					          field.tooltip( "close" ).attr( "title", "" );
				        }, 2500 );
	       			}
	       			
	       		}
    });
});