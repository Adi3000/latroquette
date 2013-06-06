$(function() {
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
});