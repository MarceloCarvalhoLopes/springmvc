$('#confirmacaoExclusaoModal').on(
		'show.bs.modal',
		function(event) {

			var buttom = $(event.relatedTarget);

			var codigoTitulo = buttom.data('codigo');
			var descricaoTitulo = buttom.data('descricao');

			var modal = $(this);
			var form = modal.find('form');
			var action = form.data('url-base');

			if (!action.endsWith('/')) {
				action += '/';
			}

			form.attr('action', action + codigoTitulo);

			modal.find('.modal-body span').html(
					'Tem certeza que deseja excluir o título <strong> '
							+ descricaoTitulo + '</strong>?');

		});

$(function() {
	$('[rel="tooltip"]').tooltip();

	$('.js-currency').maskMoney({
		decimal : ',',
		thousands : '.',
		allowZero : false
	});

	$('.js-atualizar-status')
			.on(
					'click',
					function(event) {
						event.preventDefault();

						var botaoReceber = $(event.currentTarget);
						var urlReceber = botaoReceber.attr('href');

						// console.log('urlReceber', urlReceber); Teste da url

						var response = $.ajax({
							url : urlReceber,
							type : 'PUT'

						});

						response.done(function(e) {
							var codigoTitulo = botaoReceber.data('codigo');
							
							$('[data-role =' + codigoTitulo + ']').html('<span class="label label-success">'+ e +'</span>');
							botaoReceber.hide();
								});

						response.fail(function(e) {
							console.log(e);
							alert('Erro recebendo cobrança');
						});
					});

});