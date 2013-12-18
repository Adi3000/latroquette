$(function() {
	var loadingWishesPreview = null;
	$("#wishesSelectedCategory").hide();
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
					if(ui.item.source == _keywordSource_ || ui.item.source == _externalKeywordSource_ ){
						var keywordTypeId = ui.item.source == _keywordSource_ ? _mainKeywordTypeId_ : _externalKeywordTypeId_;
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
					}else if(ui.item.source == _amazonSource_){
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
		if($(this).attr("data-source") == _amazonSource_){
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
	});
	$("#wishesMenuCategory").selectKeywordByMenu(1);
});