$(document).ready(function() {
	$('.counter .minus').on("click", function() {
		var inputElement = $(this).parent().find(".input-count");
		var count = inputElement.val();
		if (count !== '' && count !== '0') {
			count = count - 1;
		}
		inputElement.val(count);
	});

	$('.counter .plus').on("click", function() {
		var inputElement = $(this).parent().find(".input-count");
		var count = inputElement.val();
		if (count === '') {
			count = 0;
		} else {
			count = parseInt(count);
		}
		count = count + 1;
		inputElement.val(count);
	});
});
