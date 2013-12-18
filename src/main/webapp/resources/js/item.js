jQuery.fn.addItems = function(json) {
    var o = $(this[0]);
    var options = {
    		source : "#addItems > .item",
    		destination : ".itemList",
    		imagesDestination : ".itemImages",
    		itemInfos : ".itemInfos",
    		itemDetails : ".itemDetails",
    		itemTitle : ".itemTitle",
    		itemDescription : ".itemDescription",
    };
    $.each(json, function(i, data){
    	var newItem = $(options.source).clone();
    	$.each(data.imageList, function(j, image){
    		$(options.imagesDestination,newItem).append(
    				$("<img />")
    					.addClass("itemImage")
    					.attr("src","/image/p/s/"+image.name)
			);
    	});
    	var itemLink = $("<a />").attr(
    			{"href" : "/item/viewItem.jsf?item="+data.id,
    			"title"	: data.title
    			}).text(data.title);
    	
    	$(options.itemTitle,newItem).append(itemLink);
    	$(options.itemDescription,newItem).text(data.description);
    	var login = $("<div />").addClass("login").text(data.user.login);
    	$(options.itemInfos, newItem).append(login);
    	newItem.appendTo(o);
    });
    
};
$(function() {
  	if($("#itemImages > *").is("*")){
	    $("#itemImages .itemImage").rondell({
			preset: "scroller",
			theme: "light",
			center: {left: 350, top: 100 },
			center: {left: 350, top: 100 },
			visibleItems: "5",
			strings: { 
				prev : '&lt;&lt;', next : '&gt;&gt;', more: 'Plus...', 
				loadingError : 'Erreur au chargement de l\'image <strong>%s</strong>'},
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
			    displayReferencedImages: true
			} 
	    });
	}
  });