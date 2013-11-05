function addCategoryKeyword(keywordId, keywordTypeId,selectedCategoryArea){
	var keywordIdForList = keywordTypeId+_innerSeparator_+keywordId;
	$.getJSON(
		_requestContextPath_+"/rest/search/item/breadcrumb", 
		{
			i : keywordId, 
			t : keywordTypeId
		},
		function(data){
			var category = $("<li />")
							.attr("data-keyword-id", keywordId)
							.attr("data-keyword-type", keywordTypeId);
			$.each(data, function(i, parentKeyword){
					category.append(
							$("<span />")
								.attr("data-keyword-id", parentKeyword.id)
								.text(parentKeyword.name)
					);
					if(i < data.length - 1){
						category.append($("<span />").text(" > "));
					}
			});
			category.append(
				$("<span />")
					.addClass("ui-icon ui-icon-trash")
					.text("x")
					.click(function(e){
						$("#editItemForm\\:itemCategoryIds").val(removeFromStringList($("#editItemForm\\:itemCategoryIds").val(),keywordIdForList));
						var keywordToRemove =  $(this).parent("li");
						keywordToRemove.remove();
						if(selectedCategoryArea.is(":empty")){
							selectedCategoryArea.hide();
						}
					})
				);
			selectedCategoryArea.append(category);
			$("#editItemForm\\:itemCategoryIds").val(addToStringList($("#editItemForm\\:itemCategoryIds").val(),keywordIdForList));
			if(!selectedCategoryArea.is(":visible")){
				selectedCategoryArea.show(300);
			}
		}
	);
}
$(function() {
	var selectedCategoryArea = $("#selectedCategory");
	selectedCategoryArea.hide();

	$("#itemImages > *").rondell({
		preset: "scroller",
		theme: "light",
		center: {left: 350, top: 100 },
		visibleItems: "2",
		caption: true,
		strings: { 
			prev : "&lt;&lt;", next : "&gt;&gt;", more: "Plus...", 
			loadingError : "Erreur au chargement de l&sbquo;image %s"},
			repeating: true,
			autoRotation: {
				enabled: true,
				direction: 1,
				once: false,
				delay: 5000
			},
			currentLayer : 1,
			lightbox: {
				enabled: true,
				displayReferencedImages: false
			} 
	});
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
					_requestContextPath_+"/rest/search/item",
					{
						r : request.term, 
						ot: "true",
						a : "true"
					},
					function(data){
						response ( $.map(data, function(item){
							return {
								label : item.title,
								value : item.title
							};
						}));
					});
		},
		minLength : 2
	});
	$("#editItemForm #itemCategory > ul").menu({
		focus: 
			function(e, ui){
				var keyword = ui.item;
				if(!keyword.is("[data-filled='true']") && keyword.is("[data-keyword-id]")){
					$.getJSON(
						_requestContextPath_+"/rest/search/item/children", 
						{
							i : keyword.attr("data-keyword-id"), 
							t : keyword.attr("data-keyword-type")
						},
						function(data){
							console.log(data.length);
							if(data.length > 0){
								var subKeywordList = keyword.children("ul");
								$.each(data, function(i, subKeyword){
									if(!$("[data-keyword-id='"+subKeyword.id+"'][data-keyword-type='"+subKeyword.keywordTypeId+"']",subKeywordList).is("*")){
										subKeywordList.append(
											$("<li />")
												.attr("data-keyword-id",subKeyword.id)
												.attr("data-keyword-type",subKeyword.keywordTypeId)
												.append(
													$("<a />")
													.attr("href", "#")
													.text(subKeyword.name)
												)
										);
									}
								});
							}
							$("#itemMenuCategory").menu("refresh");
						}
					);
					keyword.attr("data-filled","true");
				}
			},
		select:
			function(event,ui){
				var keyword = ui.item;
				var keywordIdForList = keyword.attr("data-keyword-type")+_innerSeparator_+keyword.attr("data-keyword-id");
				if(!containsInStringList($("#editItemForm\\:itemCategoryIds").val(), keywordIdForList)){
					addCategoryKeyword(keyword.attr("data-keyword-id"),keyword.attr("data-keyword-type"),selectedCategoryArea);
				}
			}
	});
	
	
	
	if(!isEmpty($("#editItemForm\\:itemCategoryIds").val())){
		var keywordIdsList = $("#editItemForm\\:itemCategoryIds").val().split(_separator_);
		var keywordInfo;
		for(var i=0; i < keywordIdsList.length; i++){
			keywordInfo = keywordIdsList[i].split(_innerSeparator_);
			addCategoryKeyword(keywordInfo[1],keywordInfo[0],selectedCategoryArea);
		}
	}
});