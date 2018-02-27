$(document).on("click", "#open-modifierModal", function() {
	$("#groupPassword").hide();
	var userId = $(this).data('id');
	$.ajax({
		type: "get",
		async: false,
		url: "/ajax/user/" + userId,
		cache: false,
		success: function(response) {
			$("#userId").val(response.id);
			$("#nom").val(response.nom);
			$("#prenom").val(response.prenom);
			$("#email").val(response.email);
		},
		error: function() {
			alert("Error while request.");
		}
	});
});
$(document).on("click", "#open-ajouterModal", function() {
	$("#groupPassword").show();
});