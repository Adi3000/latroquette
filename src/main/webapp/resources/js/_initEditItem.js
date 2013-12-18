$(function() {
	$("#selectedCategory").hide();
	$("#editItemForm\\:choosePicButton").change(function(){
		if($(this).val() != ""){
			showLoadingDiv("Ajout de l'image en cours... Veuillez patienter...");
			$("#uploadPicButton > input").click();
		}
	});
	$("#uploadPicButton").hide(0);
	
	$("#editItemForm\\:title").autocomplete({
		source: function(request, response){
			$.getJSON(
					_requestContextPath_+"/rest/search/item/amazon",
					{
						term : request.term
					},
					function(data){
						response (data);
					});
		},
		minLength: 3,
		select:
			function (event, ui){
			$(this).val(ui.item.name);
			return false;
		},
		focus:
			function (event, ui){
			var preview = $(this).siblings(".preview");
			$("img",preview).attr("src",ui.item.imageUrl);
			if(!preview.is(":visible")){
				preview.fadeIn(300);
			}
		},
		close:
			function (event, ui){
			$(this).siblings(".preview").fadeOut(300);
		},
		create: function () {
			$(this).data("ui-autocomplete")._renderItem = function (ul, item) {
				return $("<li />")
				.attr( "data-value", item.amazonId )
				.append($("<a />")
						.append(
								$("<span />").text(item.name)
						)
				)
				.appendTo(ul);
			};
		}
	});

	$("#itemMenuCategory").selectKeywordByMenu();
	
	if(!isEmpty($("#editItemForm\\:itemCategoryIds").val())){
		var keywordIdsList = $("#editItemForm\\:itemCategoryIds").val().split(_separator_);
		var keywordInfo;
		for(var i=0; i < keywordIdsList.length; i++){
			keywordInfo = keywordIdsList[i].split(_innerSeparator_);
			addCategoryKeyword(keywordInfo[1],keywordInfo[0], $("#selectedCategory"));
		}
	}
});