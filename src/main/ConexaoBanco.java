package main;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ConexaoBanco {

	static String URL = "jdbc:mysql://localhost:3306/locacao_condominio";
	static String USER = "root";
	static String PASSWORD = "123456";
	static Scanner scanner = new Scanner(System.in); // Use um único Scanner em todo o programa

	public static Connection conexao_com_banco() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			System.err.println("ERRO AO CONECTAR NO BANCO DE DADOS: " + e.getMessage());
			return null;
		}
	}

	// Método para inserir dados na tabela morador
	public static void inserirMorador() throws SQLException {
		String nome, telefone, cpf, email, dataNascimentoInput, dataCadastroInput;
		LocalDate dataNascimento = null, dataCadastro = null;

		// Loop até o usuário preencher todos os campos corretamente
		while (true) {
			System.out.println("Digite o nome do Morador:");
			nome = scanner.nextLine();
			if (nome.trim().isEmpty()) {
				System.out.println("\033[31mNome não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			System.out.println("Digite o Telefone:");
			telefone = scanner.nextLine();
			if (telefone.trim().isEmpty()) {
				System.out.println("\033[31mTelefone não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			System.out.println("Digite a Data de Nascimento (dd/MM/yyyy):");
			dataNascimentoInput = scanner.nextLine();
			if (dataNascimentoInput.trim().isEmpty()) {
				System.out.println("\033[31mData de Nascimento não pode ser vazia! Por favor, tente novamente.\033[0m");
				continue;
			}
			try {
				dataNascimento = LocalDate.parse(dataNascimentoInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				System.out.println("\033[31mFormato de data inválido! Por favor, insira no formato dd/MM/yyyy.\033[0m");
				continue;
			}

			System.out.println("Digite o CPF:");
			cpf = scanner.nextLine();
			if (cpf.trim().isEmpty()) {
				System.out.println("\033[31mCPF não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			System.out.println("Digite o Email:");
			email = scanner.nextLine();
			if (email.trim().isEmpty()) {
				System.out.println("\033[31mEmail não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			System.out.println("Digite a Data de Cadastro (dd/MM/yyyy):");
			dataCadastroInput = scanner.nextLine();
			if (dataCadastroInput.trim().isEmpty()) {
				System.out.println("\033[31mData de Cadastro não pode ser vazia! Por favor, tente novamente.\033[0m");
				continue;
			}
			try {
				dataCadastro = LocalDate.parse(dataCadastroInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				System.out.println("\033[31mFormato de data inválido! Por favor, insira no formato dd/MM/yyyy.\033[0m");
				continue;
			}

			// Se nenhum campo estiver vazio, saímos do loop
			break;
		}

		String sql = "INSERT INTO morador(nome_morador, telefone, data_nascimento, cpf, email, data_cadastro) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {
			cursor.setString(1, nome);
			cursor.setString(2, telefone);
			cursor.setDate(3, Date.valueOf(dataNascimento));
			cursor.setString(4, cpf);
			cursor.setString(5, email);
			cursor.setDate(6, Date.valueOf(dataCadastro));

			cursor.executeUpdate();
			System.out.println("Morador inserido com sucesso!");
		} catch (SQLException e) {
			System.err.println("Erro ao inserir morador: " + e.getMessage());
		}
	}

	// Método para consultar os moradores
	public static void consultarMoradores() throws SQLException {
		String sql = "SELECT * FROM morador";

		try (Connection conexao = conexao_com_banco();
				Statement cursor = conexao.createStatement();
				ResultSet resultado = cursor.executeQuery(sql)) {

			while (resultado.next()) {
				int id = resultado.getInt("id_morador");
				String nome = resultado.getString("nome_morador");
				String telefone = resultado.getString("telefone");
				Date dataNascimento = resultado.getDate("data_nascimento");
				String cpf = resultado.getString("cpf");
				String email = resultado.getString("email");
				Date dataCadastro = resultado.getDate("data_cadastro");

				System.out.printf(
						"ID: %d, Nome: %s, Telefone: %s, Data de Nascimento: %s, CPF: %s, Email: %s, Data de Cadastro: %s%n",
						id, nome, telefone, dataNascimento, cpf, email, dataCadastro);
			}
		}
	}

	public static void atualizarMorador() throws SQLException {
		int idMorador;
		String nome, telefone, cpf, email, dataNascimentoInput, dataCadastroInput;
		LocalDate dataNascimento = null, dataCadastro = null;

		// Loop até o usuário preencher todos os campos corretamente
		while (true) {
			System.out.println("Digite o ID do Morador para atualizar:");
			try {
				idMorador = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("\033[31mID inválido! Por favor, insira um número válido.\033[0m");
				continue;
			}

			// Verificar se o ID do morador existe no banco de dados
			String verificarIdSQL = "SELECT COUNT(*) FROM morador WHERE id_morador = ?";
			try (Connection conexao = conexao_com_banco();
					PreparedStatement stmt = conexao.prepareStatement(verificarIdSQL)) {
				stmt.setInt(1, idMorador);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() && rs.getInt(1) == 0) {
					System.out.println("\033[31mID do morador não encontrado! Tente novamente.\033[0m");
					continue; // Se não encontrar o ID, solicita novo ID
				}
			} catch (SQLException e) {
				System.out.println("\033[31mErro ao verificar ID do morador!\033[0m");
				continue;
			}

			// Solicitar os dados para atualização
			System.out.println("Digite o novo nome do Morador:");
			nome = scanner.nextLine();
			if (nome.trim().isEmpty()) {
				System.out.println("\033[31mNome não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			System.out.println("Digite o novo Telefone:");
			telefone = scanner.nextLine();
			if (telefone.trim().isEmpty()) {
				System.out.println("\033[31mTelefone não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			System.out.println("Digite a nova Data de Nascimento (dd/MM/yyyy):");
			dataNascimentoInput = scanner.nextLine();
			if (dataNascimentoInput.trim().isEmpty()) {
				System.out.println("\033[31mData de Nascimento não pode ser vazia! Por favor, tente novamente.\033[0m");
				continue;
			}
			try {
				dataNascimento = LocalDate.parse(dataNascimentoInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				System.out.println("\033[31mFormato de data inválido! Por favor, insira no formato dd/MM/yyyy.\033[0m");
				continue;
			}

			System.out.println("Digite o novo CPF:");
			cpf = scanner.nextLine();
			if (cpf.trim().isEmpty()) {
				System.out.println("\033[31mCPF não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			System.out.println("Digite o novo Email:");
			email = scanner.nextLine();
			if (email.trim().isEmpty()) {
				System.out.println("\033[31mEmail não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			System.out.println("Digite a nova Data de Cadastro (dd/MM/yyyy):");
			dataCadastroInput = scanner.nextLine();
			if (dataCadastroInput.trim().isEmpty()) {
				System.out.println("\033[31mData de Cadastro não pode ser vazia! Por favor, tente novamente.\033[0m");
				continue;
			}
			try {
				dataCadastro = LocalDate.parse(dataCadastroInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				System.out.println("\033[31mFormato de data inválido! Por favor, insira no formato dd/MM/yyyy.\033[0m");
				continue;
			}

			// Se todos os campos estiverem corretos, saímos do loop
			break;
		}

		// Atualizar os dados do morador
		String sql = "UPDATE morador SET nome_morador = ?, telefone = ?, data_nascimento = ?, cpf = ?, email = ?, data_cadastro = ? WHERE id_morador = ?";

		try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {
			cursor.setString(1, nome);
			cursor.setString(2, telefone);
			cursor.setDate(3, Date.valueOf(dataNascimento));
			cursor.setString(4, cpf);
			cursor.setString(5, email);
			cursor.setDate(6, Date.valueOf(dataCadastro));
			cursor.setInt(7, idMorador);

			cursor.executeUpdate();
			System.out.println("Morador atualizado com sucesso!");
		} catch (SQLException e) {
			System.err.println("Erro ao atualizar morador: " + e.getMessage());
		}
	}

	public static void deletarMorador() throws SQLException {
		int idMorador = -1; // Inicializando com um valor inválido

		// Loop até o usuário preencher o ID corretamente
		while (true) {
			System.out.println("Digite o ID do Morador para deletar:");

			// Validação para garantir que o ID seja um número válido
			try {
				idMorador = Integer.parseInt(scanner.nextLine());
				if (idMorador <= 0) {
					System.out.println(
							"\033[31mID deve ser um número positivo válido. Por favor, tente novamente.\033[0m");
					continue;
				}
			} catch (NumberFormatException e) {
				System.out.println("\033[31mID inválido! Por favor, insira um número válido.\033[0m");
				continue;
			}

			// Verificar se o ID do morador existe no banco de dados
			String verificarIdSQL = "SELECT COUNT(*) FROM morador WHERE id_morador = ?";
			try (Connection conexao = conexao_com_banco();
					PreparedStatement stmt = conexao.prepareStatement(verificarIdSQL)) {
				stmt.setInt(1, idMorador);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() && rs.getInt(1) == 0) {
					System.out.println("\033[31mNenhum morador encontrado com o ID informado. Tente novamente.\033[0m");
					continue; // Se não encontrar o ID, solicita novo ID
				}
			} catch (SQLException e) {
				System.out.println("\033[31mErro ao verificar ID do morador!\033[0m");
				continue;
			}

			// Se o ID for válido e existir, sai do loop
			break;
		}

		// Deletar o morador
		String sql = "DELETE FROM morador WHERE id_morador = ?";

		try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {
			cursor.setInt(1, idMorador);

			int rowsAffected = cursor.executeUpdate();

			// Verifica se algum morador foi deletado
			if (rowsAffected > 0) {
				System.out.println("Morador deletado com sucesso!");
			} else {
				System.out.println("\033[31mErro inesperado ao tentar deletar o morador.\033[0m");
			}
		} catch (SQLException e) {
			System.err.println("\033[31mErro ao tentar deletar morador: " + e.getMessage() + "\033[0m");
		}
	}

	// Método para inserir uma reserva com verificação de campos vazios
	public static void inserirReserva() throws SQLException {
		LocalDate dataReserva = null, dataInicio = null, dataFim = null;
		int idMorador = 0;

		// Loop até o usuário preencher todos os campos corretamente
		while (true) {
			// Verificar Data da Reserva
			System.out.println("Digite a Data da Reserva (dd/MM/yyyy):");
			String dataReservaInput = scanner.nextLine();
			if (dataReservaInput.trim().isEmpty()) {
				System.out.println("\033[31mData da reserva não pode ser vazia!\033[0m");
				continue;
			}
			try {
				dataReserva = LocalDate.parse(dataReservaInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				System.out.println("\033[31mFormato de data inválido!\033[0m");
				continue;
			}

			// Verificar Data de Início
			System.out.println("Digite a Data de Início (dd/MM/yyyy):");
			String dataInicioInput = scanner.nextLine();
			if (dataInicioInput.trim().isEmpty()) {
				System.out.println("\033[31mData de Início não pode ser vazia!\033[0m");
				continue;
			}
			try {
				dataInicio = LocalDate.parse(dataInicioInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				System.out.println("\033[31mFormato de data inválido!\033[0m");
				continue;
			}

			// Verificar Data de Fim
			System.out.println("Digite a Data de Fim (dd/MM/yyyy):");
			String dataFimInput = scanner.nextLine();
			if (dataFimInput.trim().isEmpty()) {
				System.out.println("\033[31mData de Fim não pode ser vazia!\033[0m");
				continue;
			}
			try {
				dataFim = LocalDate.parse(dataFimInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				System.out.println("\033[31mFormato de data inválido!\033[0m");
				continue;
			}

			// Verificar ID do Morador
			System.out.println("Digite o ID do Morador:");
			String idMoradorInput = scanner.nextLine();
			if (idMoradorInput.trim().isEmpty()) {
				System.out.println("\033[31mID do morador não pode ser vazio!\033[0m");
				continue;
			}
			try {
				idMorador = Integer.parseInt(idMoradorInput);
			} catch (NumberFormatException e) {
				System.out.println("\033[31mID inválido! Deve ser um número!\033[0m");
				continue;
			}

			// Verificar se o ID do morador existe no banco de dados
			String verificarIdSQL = "SELECT COUNT(*) FROM morador WHERE id = ?";
			try (Connection conexao = conexao_com_banco();
					PreparedStatement stmt = conexao.prepareStatement(verificarIdSQL)) {
				stmt.setInt(1, idMorador);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() && rs.getInt(1) == 0) {
					System.out.println("\033[31mID do morador não encontrado!\033[0m");
					continue; // Solicita novo ID se não encontrado
				}
			} catch (SQLException e) {
				System.out.println("\033[31mErro ao verificar ID!\033[0m");
				continue;
			}

			// Se todos os campos estiverem corretos, sai do loop
			break;
		}

		// Inserir dados na tabela reserva
		String sql = "INSERT INTO reserva(data_reserva, data_inicio, data_fim, morador) VALUES (?, ?, ?, ?)";
		try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {
			cursor.setDate(1, Date.valueOf(dataReserva));
			cursor.setDate(2, Date.valueOf(dataInicio));
			cursor.setDate(3, Date.valueOf(dataFim));
			cursor.setInt(4, idMorador);

			cursor.executeUpdate();
			System.out.println("Reserva inserida com sucesso!");
		}
	}

	// Método para consultar reservas
	public static void consultarReservas() throws SQLException {
		String sql = "SELECT * FROM reserva";

		try (Connection conexao = conexao_com_banco();
				Statement cursor = conexao.createStatement();
				ResultSet resultado = cursor.executeQuery(sql)) {

			while (resultado.next()) {
				int id = resultado.getInt("id_reserva");
				Date dataReserva = resultado.getDate("data_reserva");
				Date dataInicio = resultado.getDate("data_inicio");
				Date dataFim = resultado.getDate("data_fim");
				int idMorador = resultado.getInt("morador");

				System.out.printf("ID: %d, Data Reserva: %s, Data Início: %s, Data Fim: %s, Morador: %d%n", id,
						dataReserva, dataInicio, dataFim, idMorador);
			}
		}
	}

	public static void atualizarReserva() throws SQLException {
		int idReserva = 0;
		LocalDate dataInicio = null, dataFim = null;

		// Loop até o usuário preencher todos os campos corretamente
		while (true) {
			// Verificar ID da Reserva
			System.out.println("Digite o ID da Reserva para atualizar:");
			String idReservaInput = scanner.nextLine();
			if (idReservaInput.trim().isEmpty()) {
				System.out.println("\033[31mID da reserva não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}
			try {
				idReserva = Integer.parseInt(idReservaInput);
			} catch (NumberFormatException e) {
				System.out.println("\033[31mID inválido! Por favor, insira um número válido.\033[0m");
				continue;
			}

			// Verificar se o ID da reserva existe no banco de dados
			String verificarIdSQL = "SELECT COUNT(*) FROM reserva WHERE id_reserva = ?";
			try (Connection conexao = conexao_com_banco();
					PreparedStatement stmt = conexao.prepareStatement(verificarIdSQL)) {
				stmt.setInt(1, idReserva);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() && rs.getInt(1) == 0) {
					System.out
							.println("\033[31mNenhuma reserva encontrada com o ID informado. Tente novamente.\033[0m");
					continue; // Se não encontrar o ID, solicita novo ID
				}
			} catch (SQLException e) {
				System.out.println("\033[31mErro ao verificar ID da reserva!\033[0m");
				continue;
			}

			// Verificar Data de Início
			System.out.println("Digite a nova Data de Início (dd/MM/yyyy):");
			String dataInicioInput = scanner.nextLine();
			if (dataInicioInput.trim().isEmpty()) {
				System.out.println("\033[31mData de Início não pode ser vazia! Por favor, tente novamente.\033[0m");
				continue;
			}
			try {
				dataInicio = LocalDate.parse(dataInicioInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				System.out.println("\033[31mFormato de data inválido! Por favor, insira no formato dd/MM/yyyy.\033[0m");
				continue;
			}

			// Verificar Data de Fim
			System.out.println("Digite a nova Data de Fim (dd/MM/yyyy):");
			String dataFimInput = scanner.nextLine();
			if (dataFimInput.trim().isEmpty()) {
				System.out.println("\033[31mData de Fim não pode ser vazia! Por favor, tente novamente.\033[0m");
				continue;
			}
			try {
				dataFim = LocalDate.parse(dataFimInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				System.out.println("\033[31mFormato de data inválido! Por favor, insira no formato dd/MM/yyyy.\033[0m");
				continue;
			}

			// Se todos os campos forem preenchidos corretamente, sai do loop
			break;
		}

		// SQL para atualizar os dados
		String sql = "UPDATE reserva SET data_inicio = ?, data_fim = ?, status_reserva = ? WHERE id_reserva = ?";

		try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {
			cursor.setDate(1, Date.valueOf(dataInicio));
			cursor.setDate(2, Date.valueOf(dataFim));

			// Definir um status para a reserva. Exemplo: "Confirmada"
			String statusReserva = "Confirmada"; // Ou você pode modificar conforme a lógica de status de reserva que
													// precisar
			cursor.setString(3, statusReserva);

			cursor.setInt(4, idReserva);

			cursor.executeUpdate();
			System.out.println("Reserva atualizada com sucesso!");
		} catch (SQLException e) {
			System.err.println("\033[31mErro ao atualizar reserva: " + e.getMessage() + "\033[0m");
		}
	}

	public static void deletarReserva() throws SQLException {
		int idReserva = 0;

		// Loop até o usuário preencher o ID corretamente
		while (true) {
			// Verificar ID da Reserva
			System.out.println("Digite o ID da Reserva para deletar:");
			String idReservaInput = scanner.nextLine();

			// Verifica se o ID não está vazio
			if (idReservaInput.trim().isEmpty()) {
				System.out.println("\033[31mID da reserva não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			try {
				idReserva = Integer.parseInt(idReservaInput);
			} catch (NumberFormatException e) {
				// Se o ID não for um número válido
				System.out.println("\033[31mID inválido! Por favor, insira um número válido.\033[0m");
				continue;
			}

			// Verifica se o ID da reserva existe no banco de dados
			String verificarIdSQL = "SELECT COUNT(*) FROM reserva WHERE id_reserva = ?";
			try (Connection conexao = conexao_com_banco();
					PreparedStatement stmt = conexao.prepareStatement(verificarIdSQL)) {
				stmt.setInt(1, idReserva);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() && rs.getInt(1) == 0) {
					System.out
							.println("\033[31mNenhuma reserva encontrada com o ID informado. Tente novamente.\033[0m");
					continue; // Se não encontrar o ID, solicita novo ID
				}
			} catch (SQLException e) {
				System.out.println("\033[31mErro ao verificar ID da reserva!\033[0m");
				continue;
			}

			// Se o ID for válido, sai do loop
			break;
		}

		// SQL para deletar a reserva
		String sql = "DELETE FROM reserva WHERE id_reserva = ?";

		try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {
			cursor.setInt(1, idReserva);

			// Executa a exclusão
			int rowsAffected = cursor.executeUpdate();

			// Verifica se a exclusão foi bem-sucedida
			if (rowsAffected > 0) {
				System.out.println("Reserva deletada com sucesso!");
			} else {
				System.out.println("\033[31mNenhuma reserva encontrada com o ID informado.\033[0m");
			}
		} catch (SQLException e) {
			System.err.println("\033[31mErro ao deletar reserva: " + e.getMessage() + "\033[0m");
		}
	}

	public static void inserirPagamento() throws SQLException {
		double valorPagamento = 0;
		String dataPagamentoInput, formaPagamento;
		LocalDate dataPagamento = null;
		int idReserva = 0;

		// Loop até o usuário preencher todos os campos corretamente
		while (true) {
			// Verifica o valor do pagamento
			System.out.println("Digite o Valor do Pagamento:");
			String valorPagamentoInput = scanner.nextLine();

			if (valorPagamentoInput.trim().isEmpty()) {
				System.out.println("\033[31mValor do pagamento não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			try {
				valorPagamento = Double.parseDouble(valorPagamentoInput);
			} catch (NumberFormatException e) {
				System.out.println("\033[31mValor inválido! Por favor, insira um valor numérico válido.\033[0m");
				continue;
			}

			// Verifica a data de pagamento
			System.out.println("Digite a Data do Pagamento (dd/MM/yyyy):");
			dataPagamentoInput = scanner.nextLine();
			if (dataPagamentoInput.trim().isEmpty()) {
				System.out.println("\033[31mData do pagamento não pode ser vazia! Por favor, tente novamente.\033[0m");
				continue;
			}

			try {
				dataPagamento = LocalDate.parse(dataPagamentoInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				System.out.println("\033[31mFormato de data inválido! Por favor, insira no formato dd/MM/yyyy.\033[0m");
				continue;
			}

			// Verifica a forma de pagamento
			System.out.println("Digite a Forma de Pagamento:");
			formaPagamento = scanner.nextLine();
			if (formaPagamento.trim().isEmpty()) {
				System.out.println("\033[31mForma de pagamento não pode ser vazia! Por favor, tente novamente.\033[0m");
				continue;
			}

			// Verifica o ID da reserva
			System.out.println("Digite o ID da Reserva:");
			String idReservaInput = scanner.nextLine();
			if (idReservaInput.trim().isEmpty()) {
				System.out.println("\033[31mID da reserva não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			try {
				idReserva = Integer.parseInt(idReservaInput);
			} catch (NumberFormatException e) {
				System.out.println("\033[31mID inválido! Por favor, insira um número válido.\033[0m");
				continue;
			}

			// Verifica se a reserva com o ID informado existe
			String verificarReservaSQL = "SELECT COUNT(*) FROM reserva WHERE id_reserva = ?";
			try (Connection conexao = conexao_com_banco();
					PreparedStatement stmt = conexao.prepareStatement(verificarReservaSQL)) {
				stmt.setInt(1, idReserva);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() && rs.getInt(1) == 0) {
					System.out
							.println("\033[31mNenhuma reserva encontrada com o ID informado. Tente novamente.\033[0m");
					continue; // Se não encontrar o ID, solicita novo ID
				}
			} catch (SQLException e) {
				System.out.println("\033[31mErro ao verificar o ID da reserva!\033[0m");
				continue;
			}

			// Se todos os campos foram preenchidos corretamente, sai do loop
			break;
		}

		// SQL para inserir o pagamento
		String sql = "INSERT INTO pagamento(valor_pagamento, data_pagamento, formas_pagamento, reserva) VALUES (?, ?, ?, ?)";

		try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {
			cursor.setDouble(1, valorPagamento);
			cursor.setDate(2, Date.valueOf(dataPagamento));
			cursor.setString(3, formaPagamento);
			cursor.setInt(4, idReserva);

			// Executa a inserção do pagamento
			cursor.executeUpdate();
			System.out.println("Pagamento inserido com sucesso!");
		} catch (SQLException e) {
			System.err.println("Erro ao inserir pagamento: " + e.getMessage());
		}
	}

	// Método para consultar pagamentos
	public static void consultarPagamentos() throws SQLException {
		String sql = "SELECT * FROM pagamento";

		try (Connection conexao = conexao_com_banco();
				Statement cursor = conexao.createStatement();
				ResultSet resultado = cursor.executeQuery(sql)) {

			while (resultado.next()) {
				int id = resultado.getInt("id_pagamento");
				double valor = resultado.getDouble("valor_pagamento");
				Date dataPagamento = resultado.getDate("data_pagamento");
				String formaPagamento = resultado.getString("formas_pagamento");
				int idReserva = resultado.getInt("reserva");

				System.out.printf("ID: %d, Valor: R$ %.2f, Data Pagamento: %s, Forma: %s, Reserva: %d%n", id, valor,
						dataPagamento, formaPagamento, idReserva);
			}
		}
	}

	public static void atualizarPagamento() throws SQLException {
		int idPagamento = 0;
		double valorPagamento = 0;
		String formaPagamento = null, dataPagamentoInput = null;
		LocalDate dataPagamento = null;

		// Loop até o usuário preencher todos os campos corretamente
		while (true) {
			// Verifica o ID do pagamento
			System.out.println("Digite o ID do Pagamento para atualizar:");
			String idPagamentoInput = scanner.nextLine();
			if (idPagamentoInput.trim().isEmpty()) {
				System.out.println("\033[31mID do pagamento não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			try {
				idPagamento = Integer.parseInt(idPagamentoInput);
			} catch (NumberFormatException e) {
				System.out.println("\033[31mID inválido! Por favor, insira um número válido.\033[0m");
				continue;
			}

			// Verifica se o pagamento com o ID informado existe
			String verificarPagamentoSQL = "SELECT COUNT(*) FROM pagamento WHERE id_pagamento = ?";
			try (Connection conexao = conexao_com_banco();
					PreparedStatement stmt = conexao.prepareStatement(verificarPagamentoSQL)) {
				stmt.setInt(1, idPagamento);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() && rs.getInt(1) == 0) {
					System.out
							.println("\033[31mNenhum pagamento encontrado com o ID informado. Tente novamente.\033[0m");
					continue; // Se não encontrar o ID, solicita novo ID
				}
			} catch (SQLException e) {
				System.out.println("\033[31mErro ao verificar o ID do pagamento!\033[0m");
				continue;
			}

			// Verifica o valor do pagamento
			System.out.println("Digite o novo valor do Pagamento:");
			String valorPagamentoInput = scanner.nextLine();
			if (valorPagamentoInput.trim().isEmpty()) {
				System.out.println("\033[31mValor do pagamento não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			try {
				valorPagamento = Double.parseDouble(valorPagamentoInput);
			} catch (NumberFormatException e) {
				System.out.println("\033[31mValor inválido! Por favor, insira um valor numérico válido.\033[0m");
				continue;
			}

			// Verifica a forma de pagamento
			System.out.println("Digite a nova Forma de Pagamento:");
			formaPagamento = scanner.nextLine();
			if (formaPagamento.trim().isEmpty()) {
				System.out.println("\033[31mForma de pagamento não pode ser vazia! Por favor, tente novamente.\033[0m");
				continue;
			}

			// Verifica a data de pagamento
			System.out.println("Digite a nova Data de Pagamento (dd/MM/yyyy):");
			dataPagamentoInput = scanner.nextLine();
			if (dataPagamentoInput.trim().isEmpty()) {
				System.out.println("\033[31mData do pagamento não pode ser vazia! Por favor, tente novamente.\033[0m");
				continue;
			}

			try {
				dataPagamento = LocalDate.parse(dataPagamentoInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (Exception e) {
				System.out.println("\033[31mFormato de data inválido! Por favor, insira no formato dd/MM/yyyy.\033[0m");
				continue;
			}

			// Se nenhum campo estiver vazio ou inválido, sai do loop
			break;
		}

		// SQL para atualizar o pagamento
		String sql = "UPDATE pagamento SET valor_pagamento = ?, formas_pagamento = ?, data_pagamento = ? WHERE id_pagamento = ?";

		try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {
			cursor.setDouble(1, valorPagamento);
			cursor.setString(2, formaPagamento);
			cursor.setDate(3, Date.valueOf(dataPagamento));
			cursor.setInt(4, idPagamento);

			// Executa a atualização do pagamento
			cursor.executeUpdate();
			System.out.println("Pagamento atualizado com sucesso!");
		} catch (SQLException e) {
			System.err.println("Erro ao atualizar pagamento: " + e.getMessage());
		}
	}

	public static void deletarPagamento() throws SQLException {
		int idPagamento = 0;

		// Loop até o usuário preencher o campo corretamente
		while (true) {
			System.out.println("Digite o ID do Pagamento para deletar:");
			String idPagamentoInput = scanner.nextLine();

			// Verifica se o campo está vazio
			if (idPagamentoInput.trim().isEmpty()) {
				System.out.println("\033[31mID do pagamento não pode ser vazio! Por favor, tente novamente.\033[0m");
				continue;
			}

			// Verifica se o ID é um número válido
			try {
				idPagamento = Integer.parseInt(idPagamentoInput);
			} catch (NumberFormatException e) {
				System.out.println("\033[31mID inválido! Por favor, insira um número válido.\033[0m");
				continue;
			}

			// Verifica se o pagamento com o ID informado existe
			String verificarPagamentoSQL = "SELECT COUNT(*) FROM pagamento WHERE id_pagamento = ?";
			try (Connection conexao = conexao_com_banco();
					PreparedStatement stmt = conexao.prepareStatement(verificarPagamentoSQL)) {
				stmt.setInt(1, idPagamento);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() && rs.getInt(1) == 0) {
					System.out
							.println("\033[31mNenhum pagamento encontrado com o ID informado. Tente novamente.\033[0m");
					continue; // Se não encontrar o ID, solicita novo ID
				}
			} catch (SQLException e) {
				System.out.println("\033[31mErro ao verificar o ID do pagamento!\033[0m");
				continue;
			}

			// Se o campo não for vazio e o ID for válido, sai do loop
			break;
		}

		// SQL para deletar o pagamento
		String sql = "DELETE FROM pagamento WHERE id_pagamento = ?";

		try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {
			cursor.setInt(1, idPagamento);

			// Executa a exclusão do pagamento
			cursor.executeUpdate();
			System.out.println("Pagamento deletado com sucesso!");
		} catch (SQLException e) {
			System.err.println("Erro ao deletar pagamento: " + e.getMessage());
		}
	}

	public static void main(String[] args) throws SQLException {
		int opcao = 0;
		while (true) {
			String verde = "\033[32m";
			String reset = "\033[0m";

			System.out.println(verde + "Bem-vindo ao Sistema de Locação de Condomnio. Escolha uma opção:\n"
					+ "1 - Inserir Morador\n" + "2 - Consultar Moradores\n" + "3 - Atualizar Morador\n"
					+ "4 - Deletar Morador\n" + "5 - Inserir Reserva\n" + "6 - Consultar Reservas\n"
					+ "7 - Atualizar Reserva\n" + "8 - Deletar Reserva\n" + "9 - Inserir Pagamento\n"
					+ "10 - Consultar Pagamentos\n" + "11 - Atualizar Pagamento\n" + "12 - Deletar Pagamento\n"
					+ "13 - Sair" + reset);

			try {
				opcao = Integer.parseInt(scanner.nextLine());
			} catch (Exception e) {
				System.out.println("\033[35mDigite um número válido.\033[0m");
				System.out.println("\033[35mAperte Enter para Sair >>> (MENU)\033[0m");
				String sonumero = scanner.nextLine();
				continue;
			}

			switch (opcao) {
			case 1:
				inserirMorador();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String inserirmorador = scanner.nextLine();
				break;
			case 2:
				consultarMoradores();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String morador = scanner.nextLine();
				break;
			case 3:
				atualizarMorador();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String atualizarmorador = scanner.nextLine();
				break;
			case 4:
				deletarMorador();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String deletemorador = scanner.nextLine();
				break;
			case 5:
				inserirReserva();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String inserirreserva = scanner.nextLine();
				break;
			case 6:
				consultarReservas();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String reserva = scanner.nextLine();
				break;
			case 7:
				atualizarReserva();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String atualizarreserva = scanner.nextLine();
				break;
			case 8:
				deletarReserva();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String deletarreserva = scanner.nextLine();
				break;
			case 9:
				inserirPagamento();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String inserirpagamento = scanner.nextLine();
				break;
			case 10:
				consultarPagamentos();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String pagamento = scanner.nextLine();
				break;
			case 11:
				atualizarPagamento();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String atualizarpagamento = scanner.nextLine();
				break;
			case 12:
				deletarPagamento();
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String deletarpagamento = scanner.nextLine();
				break;
			case 13:
				System.out.println("\033[33mSaindo do Programa!\033[0m");
				return;
			default:
				System.out.println("\033[36mOpção inválida. Tente novamente.\033[0m");
				System.out.println("\033[31mAperte Enter para Sair >>> (MENU)\033[0m");
				String opcaoinvalida = scanner.nextLine();
				break;
			}
		}
	}
}