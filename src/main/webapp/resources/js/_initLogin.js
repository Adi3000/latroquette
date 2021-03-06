$(function() {
	$("#loginDiv").hide(0);
	$("#passwordConfirm").hide(0);
	$("#forgotPassword").hide(0);
	$("#switchToLogin").click(function(){
		$("#loginDiv").slideDown(600,"easeOutBounce");
	});
	$("#switchToLogin").click(function(){
		$("#arrowDiv").hide(600);
	});
	$("#switchToArrow").click(function(){
		$("#arrowDiv").show(600);
	});
	$("#switchToArrow").click(function(){
		$("#loginDiv").slideUp(600);
	});
	$("#forgotPasswordButton a").click(function(){
	    $("#forgotPassword").slideDown(600,"easeOutBounce");
	    return false;
	});
	$("#forgotPasswordClose").click(function(){
	    $("#forgotPassword").slideUp(600);
	});
	$("#displayRegister").click(function(){
		if(registerDisplayed){
			$("#registerBorder").slideUp(400, function(){
				$("#loginForm").animate({'width' : 344 }); 
			});
			$(this).val("S'inscrire");
			$("#loginTitle").show(0);
			$("#registerTitle").hide(0);
			$("#loginButton").show(0).removeAttr("disabled");
			$("#registerButton").hide(0).attr("disabled","disabled");
			$("#passwordConfirm").hide(400);
			registerDisplayed = false;
		}else{
			$("#loginForm").animate({'width' : 700 }, function(){
				$("#registerBorder").slideDown(400);
			});
			$(this).val("Annuler");
			$("#loginTitle").hide(0);
			$("#loginButton").hide(0).attr("disabled","disabled");
			$("#registerTitle").show(0);
			$("#registerButton").show(0).removeAttr("disabled");
			$("#passwordConfirm").show(400);
			registerDisplayed = true;
		}
	});
	$("#registerBorder").hide(0);
	$("#registerTitle").hide(0);
	$("#registerButton").hide(0).attr("disabled","disabled");
	if(forceLogin  == "true"){
		$("#switchToLogin").click();
	}
});