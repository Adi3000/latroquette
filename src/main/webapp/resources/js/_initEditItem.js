$(function() {
	$("#selectedCategory").hide();
	$("#wishesSelectedCategory").hide();
	$("#logo").tooltip({
			content : "Haha tu vois !",
			open: function(){ console.log("Jsuis ouvert !");}
		});
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
	var loadingWishesPreview = null;
	$("#wishesForm\\:wishField").autocomplete({
		source: function(request, response){
			$.getJSON(
					_requestContextPath_+"/rest/search/item/wish",
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
			$("#wishesForm\\:wishCode").val(ui.item.uid);
			$("#wishesForm\\:wishSource").val(ui.item.source);
			$("#wishesForm\\:whishesCategoryIds").val("");
			$("#wishesMenuCategory").menu("option","disabled",true);
			return false;
		},
		change:
			function(event, ui){
	   			if(ui.item) {
	   				$(this).val(ui.item.name);
	   				$("#wishesForm\\:wishCode").val(ui.item.uid);
	   				$("#wishesForm\\:wishSource").val(ui.item.source);
	   				$("#wishesForm\\:whishesCategoryIds").val("");
	   				$("#wishesMenuCategory").menu("option","disabled",true);
	   			}else{
	   				$("#wishesForm\\:wishCode").val("");
	   				$("#wishesForm\\:wishSource").val("");
	   				$("#wishesMenuCategory").menu("option","disabled",false);
	   			}
	   		},
		focus:
			function (event, ui){
				clearTimeout(loadingWishesPreview);
				if(!ui.item.desc || ui.item.desc != null){
					if(ui.item.source == "key" || ui.item.source == "xey" ){
						var keywordTypeId = ui.item.source == "key" ? 1 : 3;
						var keywordId = ui.item.uid;
						$.getJSON(
							_requestContextPath_+"/rest/search/item/breadcrumb", 
							{
								i : keywordId, 
								t : keywordTypeId
							},
							function(data){
								var desc = null;
								desc = $("<div />")
												.attr("data-keyword-id", keywordId)
												.attr("data-keyword-type", keywordTypeId);
								desc.appendBreadcrumb(data);
								ui.item.desc = desc;
							}
						);
					}else if(ui.item.source == "amz"){
						$.getJSON(
							_requestContextPath_+"/rest/search/item/amazon/byId", 
							{
								id : ui.item.uid 
							},
							function(data){
								if(data && data != null && !isEmpty(data)){
									var desc = null;
									desc = $("<div />")
												.append($("<img />").attr("src",data.imageUrl ));
									ui.item.desc = desc;
								}
							}
						);		
					}
				}
				//Give time to item description to be loaded
				loadingWishesPreview = setTimeout(function(){
					if(ui.item.desc && ui.item.desc != null){
						var preview = $("#wishesForm\\:wishField").siblings(".preview");
						preview.empty();
						preview.append(ui.item.desc);
						if(!preview.is(":visible")){
							preview.fadeIn(300);
						}
					}
				},500);
			},
		close:
			function (event, ui){
				clearTimeout(loadingWishesPreview);
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
	$("#wishesForm").on("mouseover", ".wishedItem", function(){
		var desc = null;
		var item = $(this);
		clearTimeout(loadingWishesPreview);
		if($(this).attr("data-source") == "amz"){
			$.getJSON(
				_requestContextPath_+"/rest/search/item/amazon/byId", 
				{
					id : $(this).attr("data-uid") 
				},
				function(data){
					if(data && data != null && !isEmpty(data)){
						desc = $("<div />")
									.append($("<img />").attr("src",data.imageUrl ));
						if(!item.attr("data-done")){
							item.append($("<a />").attr("href",data.sourceUrl).attr("title"," A partir de "+data.price+" € sur Amazon").text("[voir sur Amazon]"));
							item.attr("data-done", true);
						}
					}
				}
			);
		}
		//Give time to item description to be loaded
		loadingWishesPreview = setTimeout(function(){
			if(desc && desc != null){
				var preview = $("#wishesForm\\:wishField").siblings(".preview");
				preview.empty();
				preview.append(desc);
				if(!preview.is(":visible")){
					preview.fadeIn(300);
				}
			}
		},500);
	}).on("mouseout", ".wishedItem", function(){
		clearTimeout(loadingWishesPreview);
		$("#wishesForm\\:wishField").siblings(".preview").fadeOut(300);
	})
	$("#itemMenuCategory").selectKeywordByMenu();
	$("#wishesMenuCategory").selectKeywordByMenu(1);
	//To be able to prevent some bugs
	$(".preview").click(function(){
		$(this).fadeOut(300);
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