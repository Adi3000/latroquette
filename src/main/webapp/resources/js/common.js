/**
 * Show a loading message
 * @param text
 */
function showLoadingDiv(text){
	$("#loadingText").text(text);
	$("#loadingDiv").show();
}
function limit(text, maxLength){
	console.log(text.length);
	console.log(maxLength);
	if(text.length <= maxLength){
		return text;
	}else{
		//trim the string to the maximum length
		var trimmedString = text.substr(0, maxLength);

		//re-trim if we are in the middle of a word
		trimmedString = trimmedString.substr(0, Math.min(trimmedString.length, trimmedString.lastIndexOf(" ")));
		console.log(trimmedString);
		return trimmedString + "...";
	}
}
/**
 * Display an error message within content with specified code error
 * @param code
 * @param content
 */
function errorMessage(code,content){
	if(!$("#errorDiv [data-error-code="+code+"]").is("*")){
		$("#errorDiv")
				.append(
					$("<div />")
						.attr("data-error-code",code)
						.addClass(".error")
						.click(function(){
							$(this).remove();
							if($("#errorDiv").is(":empty")){
								$("#errorDiv").fadeOut(300);
							}
						})
						.append($('<span />').addClass("ui-icon ui-icon-alert"))
						.append(content));
	}else{
		$("#errorDiv").effect("highlight", {color : "#FF9999"});
		$("#errorDiv [data-error-code="+code+"]").effect("highlight");
	}
	if(!$("#errorDiv").is(":visible")){
		$("#errorDiv").fadeIn(300);
	}
}
/**
 * Remove an error message within code specified
 * @param code
 * @param content
 */
function removeErrorMessage(code){
	if($("#errorDiv [data-error-code="+code+"]").is("*")){
		$("#errorDiv [data-error-code="+code+"]").remove();
		if($("#errorDiv").is(":empty")){
			$("#errorDiv").fadeOut(300);
		}
	}
}
/**
 * Hide loading div
 */
function hideLoadingDiv(){
	$("#loadingDiv").hide();
}
/**
 * Check if o is an empty object (if array is empty, or string is empty)
 * @param o
 * @returns
 */
function isEmpty(o){
	if( Object.prototype.toString.call( o ) === '[object Array]' && o.length == 1){
		return $.isEmptyObject(o[0]);
	}else{
		return $.isEmptyObject(o);
	}
}
function formatTimestamp(timestamp){
	var date = new Date(timestamp * 1000),
		datevalues = [
	       date.getFullYear()
	      ,date.getMonth()+1
	      ,date.getDate()
	      ,date.getHours()
	      ,date.getMinutes()
	      ,date.getSeconds()
	   ];
	return datevalues;
}
/**
 * Check if list contains e
 * @param list
 * @param e
 * @returns {Boolean}
 */
function containsInStringList(list, e){
	if(isEmpty(list)){
		return false;
	}
	var check = getRegExMatcher(e);
	return list.match(check[0]) != null || list.match(check[1]) != null;
}
/**
 * Build a regex to match occurence of e
 * in a StringList
 * @param e
 */
function getRegExMatcher(e){
	var regExMatcher = new Array();
	regExMatcher[0] = new RegExp(_separator_+e+"$|^"+e+"("+_separator_+")?");
	regExMatcher[1] = new RegExp(_separator_+e+_separator_);
	return regExMatcher;
}
/**
 * Remove e from StringList
 * @param list
 * @param e
 * @returns
 */
function removeFromStringList(list, e){
	if(!isEmpty(list)){
		var check = getRegExMatcher(e);
		list = list.replace(check[0],"");
		list = list.replace(check[1],_separator_);
	}
	return list;
}
/**
 * Add an element e to the StringList list 
 * @param list
 * @param e
 * @returns
 */
function addToStringList(list, e){
	if(isEmpty(list)){
		list = list + e;
	}else{
		list = list + _separator_ + e;
	}
	return list;
}

/**
 * Add a keyword in StringList format with its type
 * and push it to be displayed under the SelectedCategoryArea
 * @param keywordId
 * @param keywordTypeId
 * @param selectedCategoryArea
 */
function addCategoryKeyword(keywordId, keywordTypeId,selectedCategoryArea){
	var keywordIdForList = keywordTypeId+_innerSeparator_+keywordId;
	var $inputCategoryIds = selectedCategoryArea.siblings("input[type=hidden]").is("*") ?
			 selectedCategoryArea.siblings("input[type=hidden]") : 
			 selectedCategoryArea.siblings(".categoryIds").children("input[type=hidden]");
	$.getJSON(
		_requestContextPath_+"/rest/search/item/breadcrumb", 
		{
			i : keywordId, 
			t : keywordTypeId
		},
		function(data){
			renderCategoryKeyword(data, keywordId, keywordTypeId,selectedCategoryArea, $inputCategoryIds);
			$inputCategoryIds.val(addToStringList($inputCategoryIds.val(),keywordIdForList));
		}
	);
}
/**
 * Render a category Keyword into selectedCategoryArea element with data for breadcrumb infos
 * Use inputCategoryIds.val() element as reference when adding removal event
 * @param data
 * @param keywordId
 * @param keywordTypeId
 * @param selectedCategoryArea
 * @param inputCategoryIds
 */
function renderCategoryKeyword(data,keywordId, keywordTypeId,selectedCategoryArea, inputCategoryIds){
	var category = $("<li />")
		.attr("data-keyword-id", keywordId)
		.attr("data-keyword-type", keywordTypeId);
	category.appendBreadcrumb(data);
	category.prepend(
	$("<span />")
		.addClass("ui-icon ui-icon-trash")
		.text("x")
		//adding click for removal event
		.click(function(e){
			inputCategoryIds.val(removeFromStringList(inputCategoryIds.val(),keywordIdForList));
			var keywordToRemove =  $(this).parent("li");
			keywordToRemove.remove();
			if(selectedCategoryArea.is(":empty")){
				selectedCategoryArea.hide();
			}
			removeErrorMessage(selectedCategoryArea.attr("id")+"_is_full");
		})
	);
	selectedCategoryArea.append(category);
	if(!selectedCategoryArea.is(":visible")){
		selectedCategoryArea.show(300);
	}
}
$(function(){
	$.fn.extend({
		/**
		 * Append a breadcrumb from a data
		 * list issued from a Breadcrumb object
		 * on div
		 * @param div
		 * @param data
		 */
		appendBreadcrumb: function(data){
			var div = this;
			$.each(data, function(i, parentKeyword){
				div.append(
						$("<span />")
						.attr("data-keyword-id", parentKeyword.id)
						.text(parentKeyword.name)
				);
				if(i < data.length - 1){
					div.append($("<span />").text(" > "));
				}
			});
		},
		/**
		 * Add a menu for selecting some keyword
		 */
		selectKeywordByMenu: function(max,url){
			var $selectedCategoryArea = $(this).siblings(".selectedCategories");
			if(!url || url == null || url == ""){
				url = _requestContextPath_+"/rest/search/item/children";
			}
			if(!max || max == null || max == 0){
				max = -1;
			}
			this.menu({
				focus: 
					function(e, ui){
						var $menu = $(this);
						var keyword = ui.item;
						if(!keyword.is("[data-filled='true']") && keyword.is("[data-keyword-id]")){
							$.getJSON(
								url, 
								{
									i : keyword.attr("data-keyword-id"), 
									t : keyword.attr("data-keyword-type")
								},
								function(data){
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
														).append($("<ul />"))
												);
											}
										});
									}
									$menu.menu("refresh");
									keyword.attr("data-filled","true");
								}
							);
						}
					},
				select:
					function(event,ui){
						var $inputCategoryIds = $(this).siblings("input[type=hidden]").is("*") ?
									$(this).siblings("input[type=hidden]") : 
									$(this).siblings(".categoryIds").children("input[type=hidden]");
						var keyword = ui.item;
						if(isEmpty(keyword.attr("data-keyword-id"))){
							return false;
						}
						if(max <= 0 || $("li",$selectedCategoryArea).length < max){
							var keywordIdForList = keyword.attr("data-keyword-type")+_innerSeparator_+keyword.attr("data-keyword-id");
							if(!containsInStringList($inputCategoryIds.val(), keywordIdForList)){
								addCategoryKeyword(keyword.attr("data-keyword-id"),keyword.attr("data-keyword-type"),$selectedCategoryArea);
							}
						}else{
							errorMessage($selectedCategoryArea.attr("id")+"_is_full","Vous ne pouvez choisir que " + max + " catégories");
						}
						return false;
					}
			});
		},
		userInfo:
			function(){
				var userLogin = $(this).attr("data-login");
				var $this = $(this);
				if(!isEmpty(userLogin)){
					$.getJSON(
							_requestContextPath_+"/rest/authentication/userInfo",
							{
								login : userLogin,
							},
							function(user){
								var $div = $("<div />").addClass("user-card");
								if(user.place){
									$div.append($("<div />")
											.addClass("place")
											.append($("<span />")
												.text(user.place.name + " (" + user.place.postalCodesList + ")")));
								}
								$div.append($("<div />")
									.addClass("profile")
									.append($("<a />")
										.attr("href","/item/index.jsf?o="+user.id)
										.text("Voir tous ses articles")));
								if(typeof converse != 'undefined'){	
									$div.append($("<div />")
										.addClass("xmpp")
										.append($("<span />")
											.addClass("xmpp-add")
											.click(function(){
												converse.addContact(user.login);
											})
											.text("Ajouter aux contacts instantanés")));
								}
								$this.after($div);
							}
					);
					$this.powerTip({
						placement : 'se',
						mouseOnToPopup : true
					})
					.data('powertipjq', function(){
						var tip = $(this).siblings(".user-card").clone();
						tip.css("display", "block");
						return tip;
					});
				}
			}
		
	});
	$( "input[type=submit],input[type=button], a.button, button" ).each(function(){
		$(this).button();
		if($(this).hasClass("disabled")){
			$(this).button( "option", "disabled", true );
		};
	});
	if($(".userInfo").is("*")){
		$(".userInfo").userInfo();
	}
	//To be able to prevent some bugs
	$(".preview").click(function(){
		$(this).fadeOut(300);
	});
});

/**
 * Protect window.console method calls, e.g. console is not defined on IE
 * unless dev tools are open, and IE doesn't define console.debug
 */
(function() {
  if (!window.console) {
    window.console = {};
  }
  // union of Chrome, FF, IE, and Safari console methods
  var m = [
    "log", "info", "warn", "error", "debug", "trace", "dir", "group",
    "groupCollapsed", "groupEnd", "time", "timeEnd", "profile", "profileEnd",
    "dirxml", "assert", "count", "markTimeline", "timeStamp", "clear"
  ];
  // define undefined methods as noops to prevent errors
  for (var i = 0; i < m.length; i++) {
    if (!window.console[m[i]]) {
      window.console[m[i]] = function() {};
    }    
  } 
})();
