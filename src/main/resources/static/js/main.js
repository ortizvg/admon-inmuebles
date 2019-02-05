 $(document).ready(function() {
    
    //Filtro para tablas
    $("#filtroInputId").on("keyup", function() {
	    var inputValue = $(this).val().toLowerCase();
	    $("#tableId tbody tr ").filter(function() {
	    	$(this).toggle($(this).text().toLowerCase().indexOf(inputValue) > -1)
	    });
	});
	
	
	//Pagination
	$('#demo-foo-pagination').footable();
	$('#demo-show-entries').change(function (e) {
		e.preventDefault();
		var pageSize = $(this).val();
		$('#demo-foo-pagination').data('page-size', pageSize);
		$('#demo-foo-pagination').trigger('footable_initialized');
	});

	
	var addrow = $('#tableId');
	addrow.footable().on('click', '.delete-row-btn', function() {
		var footable = addrow.data('footable');
		var row = $(this).parents('tr:first');
		footable.removeRow(row);
	});
    
  });