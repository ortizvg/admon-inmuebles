function addControlState(div, emptyMessage) {
	var input = div.getElementsByClassName("form-control")[0];
	input.addEventListener('blur', function() {
		this.checkValidity();
		if (this.validity.valid) {
			addStateClass(div, '', false)
		}
	}, false);

	input.addEventListener('change', function() {
		if (this.value.trim() === '') {
			this.value = '';
		}
		this.checkValidity();
		if (this.validity.valid) {
			addStateClass(div, '', false)
		}
	}, false);
	
	input.addEventListener('keyup', function() {
		if (this.value.trim() === '') {
			this.value = '';
		}
		this.checkValidity();
		if (this.validity.valid) {
			addStateClass(div, '', false)
		}
	}, false);

	input.addEventListener('invalid', function() {
		if (this.validity.valueMissing) {
			addStateClass(div, emptyMessage, true)
		} else if (input.validity.patternMismatch) {
			addStateClass(div, this.title, true)
		} else {
			addStateClass(div, '', false)
		}
	}, false);
}

function addStateClass(div, message, error) {
	var input = div.getElementsByClassName("form-control")[0];
	var helpBlock = div.getElementsByClassName('form-control-feedback')[0];
	$(helpBlock).text(message);
	if (error) {
		$(div).removeClass('has-success').addClass('has-danger');
		$(input).removeClass('form-control-success').addClass('form-control-danger');
		input.setCustomValidity(' ');
	} else {
		$(div).removeClass('has-danger').addClass('has-success');
		$(input).removeClass('form-control-danger').addClass('form-control-success');
		input.setCustomValidity('');
	}
}