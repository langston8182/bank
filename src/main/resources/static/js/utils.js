function confirm(text, url) {
	bootbox.confirm(text, function(result) {
		if (result) {
			window.location.href = url;
		}
	});
}