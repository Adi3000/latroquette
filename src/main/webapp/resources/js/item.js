jQuery.fn.addItems = function(json) {
    var o = $(this[0]);
    var options = {
    		source : "#addItems > .item",
    		destination : "#itemList",
    		imagesDestination : "#itemImages",
    		itemInfos : "#itemInfos",
    		itemDetails : "#itemDetails"
    };
    var newItem = $(options.source).clone();
    newItem.appendTo(o);
    
};

