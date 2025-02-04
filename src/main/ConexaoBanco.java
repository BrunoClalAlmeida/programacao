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
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o nome do Morador:");
			String nome = scanner.nextLine();

			System.out.println("Digite o Telefone:");
			String telefone = scanner.nextLine();

			System.out.println("Digite a Data de Nascimento (dd/MM/yyyy):");
			String dataNascimentoInput = scanner.nextLine();
			LocalDate dataNascimento = LocalDate.parse(dataNascimentoInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			System.out.println("Digite o CPF:");
			String cpf = scanner.nextLine();

			System.out.println("Digite o Email:");
			String email = scanner.nextLine();

			System.out.println("Digite a Data de Cadastro (dd/MM/yyyy):");
			String dataCadastroInput = scanner.nextLine();
			LocalDate dataCadastro = LocalDate.parse(dataCadastroInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

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

			}
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

	// Método para atualizar dados do morador
	public static void atualizarMorador() throws SQLException {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o ID do Morador para atualizar:");
			int idMorador = Integer.parseInt(scanner.nextLine());

			System.out.println("Digite o novo nome do Morador:");
			String nome = scanner.nextLine();

			System.out.println("Digite o novo Telefone:");
			String telefone = scanner.nextLine();

			System.out.println("Digite a nova Data de Nascimento (dd/MM/yyyy):");
			String dataNascimentoInput = scanner.nextLine();
			LocalDate dataNascimento = LocalDate.parse(dataNascimentoInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			System.out.println("Digite o novo CPF:");
			String cpf = scanner.nextLine();

			System.out.println("Digite o novo Email:");
			String email = scanner.nextLine();

			System.out.println("Digite a nova Data de Cadastro (dd/MM/yyyy):");
			String dataCadastroInput = scanner.nextLine();
			LocalDate dataCadastro = LocalDate.parse(dataCadastroInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

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
			}
		}
	}

	// Método para deletar um morador
	public static void deletarMorador() throws SQLException {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o ID do Morador para deletar:");
			int idMorador = Integer.parseInt(scanner.nextLine());

			String sql = "DELETE FROM morador WHERE id_morador = ?";

			try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {

				cursor.setInt(1, idMorador);

				cursor.executeUpdate();
				System.out.println("Morador deletado com sucesso!");
			}
		}
	}

	// Método para inserir uma reserva
	public static void inserirReserva() throws SQLException {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite a Data da Reserva (dd/MM/yyyy):");
			String dataReservaInput = scanner.nextLine();
			LocalDate dataReserva = LocalDate.parse(dataReservaInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			System.out.println("Digite a Data de Início (dd/MM/yyyy):");
			String dataInicioInput = scanner.nextLine();
			LocalDate dataInicio = LocalDate.parse(dataInicioInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			System.out.println("Digite a Data de Fim (dd/MM/yyyy):");
			String dataFimInput = scanner.nextLine();
			LocalDate dataFim = LocalDate.parse(dataFimInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			System.out.println("Digite o ID do Morador:");
			int idMorador = Integer.parseInt(scanner.nextLine());

			String sql = "INSERT INTO reserva(data_reserva, data_inicio, data_fim,morador) VALUES (?, ?, ?, ?)";

			try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {

				cursor.setDate(1, Date.valueOf(dataReserva));
				cursor.setDate(2, Date.valueOf(dataInicio));
				cursor.setDate(3, Date.valueOf(dataFim));
				cursor.setInt(4, idMorador);

				cursor.executeUpdate();
				System.out.println("Reserva inserida com sucesso!");
			}
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

	// Método para atualizar dados da reserva
	public static void atualizarReserva() throws SQLException {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o ID da Reserva para atualizar:");
			int idReserva = Integer.parseInt(scanner.nextLine());

			System.out.println("Digite a nova Data de Início (dd/MM/yyyy):");
			String dataInicioInput = scanner.nextLine();
			LocalDate dataInicio = LocalDate.parse(dataInicioInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			System.out.println("Digite a nova Data de Fim (dd/MM/yyyy):");
			String dataFimInput = scanner.nextLine();
			LocalDate dataFim = LocalDate.parse(dataFimInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			String sql = "UPDATE reserva SET data_inicio = ?, data_fim = ?, status_reserva = ? WHERE id_reserva = ?";

			try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {

				cursor.setDate(1, Date.valueOf(dataInicio));
				cursor.setDate(2, Date.valueOf(dataFim));
				cursor.setInt(3, idReserva);

				cursor.executeUpdate();
				System.out.println("Reserva atualizada com sucesso!");
			}
		}
	}

	// Método para deletar uma reserva
	public static void deletarReserva() throws SQLException {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o ID da Reserva para deletar:");
			int idReserva = Integer.parseInt(scanner.nextLine());

			String sql = "DELETE FROM reserva WHERE id_reserva = ?";

			try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {

				cursor.setInt(1, idReserva);

				cursor.executeUpdate();
				System.out.println("Reserva deletada com sucesso!");
			}
		}
	}

	// Método para inserir um pagamento
	public static void inserirPagamento() throws SQLException {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o Valor do Pagamento:");
			double valorPagamento = Double.parseDouble(scanner.nextLine());

			System.out.println("Digite a Data do Pagamento (dd/MM/yyyy):");
			String dataPagamentoInput = scanner.nextLine();
			LocalDate dataPagamento = LocalDate.parse(dataPagamentoInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			System.out.println("Digite a Forma de Pagamento:");
			String formaPagamento = scanner.nextLine();

			System.out.println("Digite o ID da Reserva:");
			int idReserva = Integer.parseInt(scanner.nextLine());

			String sql = "INSERT INTO pagamento(valor_pagamento, data_pagamento, formas_pagamento, reserva) VALUES (?, ?, ?, ?)";

			try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {

				cursor.setDouble(1, valorPagamento);
				cursor.setDate(2, Date.valueOf(dataPagamento));
				cursor.setString(3, formaPagamento);
				cursor.setInt(4, idReserva);

				cursor.executeUpdate();
				System.out.println("Pagamento inserido com sucesso!");
			}
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

	// Método para atualizar pagamento
	public static void atualizarPagamento() throws SQLException {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o ID do Pagamento para atualizar:");
			int idPagamento = Integer.parseInt(scanner.nextLine());

			System.out.println("Digite o novo valor do Pagamento:");
			double valorPagamento = Double.parseDouble(scanner.nextLine());

			System.out.println("Digite a nova Forma de Pagamento:");
			String formaPagamento = scanner.nextLine();

			System.out.println("Digite a nova Data de Pagamento (dd/MM/yyyy):");
			String dataPagamentoInput = scanner.nextLine();
			LocalDate dataPagamento = LocalDate.parse(dataPagamentoInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			String sql = "UPDATE pagamento SET valor_pagamento = ?, formas_pagamento = ?, data_pagamento = ? WHERE id_pagamento = ?";

			try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {

				cursor.setDouble(1, valorPagamento);
				cursor.setString(2, formaPagamento);
				cursor.setDate(3, Date.valueOf(dataPagamento));
				cursor.setInt(4, idPagamento);

				cursor.executeUpdate();
				System.out.println("Pagamento atualizado com sucesso!");
			}
		}
	}

	// Método para deletar pagamento
	public static void deletarPagamento() throws SQLException {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o ID do Pagamento para deletar:");
			int idPagamento = Integer.parseInt(scanner.nextLine());

			String sql = "DELETE FROM pagamento WHERE id_pagamento = ?";

			try (Connection conexao = conexao_com_banco(); PreparedStatement cursor = conexao.prepareStatement(sql)) {

				cursor.setInt(1, idPagamento);

				cursor.executeUpdate();
				System.out.println("Pagamento deletado com sucesso!");
			}
		}
	}

	public static void main(String[] args) throws SQLException {
		while (true) {
			System.out.println(
					"Bem-vindo ao Sistema de Locação de Condomínio. Escolha uma opção:\n" + "1 - Inserir Morador\n"
							+ "2 - Consultar Moradores\n" + "3 - Atualizar Morador\n" + "4 - Deletar Morador\n"
							+ "5 - Inserir Reserva\n" + "6 - Consultar Reservas\n" + "7 - Atualizar Reserva\n"
							+ "8 - Deletar Reserva\n" + "9 - Inserir Pagamento\n" + "10 - Consultar Pagamentos\n"
							+ "11 - Atualizar Pagamento\n" + "12 - Deletar Pagamento\n" + "13 - Sair");
			Scanner scanner = new Scanner(System.in);
			int opcao = 0;
			try {
				opcao = Integer.parseInt(scanner.nextLine());
			} catch (Exception e) {
				System.out.println("Digite um número válido.");
			}

			switch (opcao) {
			case 1:
				inserirMorador();
				break;

			case 2:
				consultarMoradores();
				System.out.println("Digite Sair para ir ao Menu:");
				String sairMorador = scanner.nextLine();
				break;

			case 3:
				atualizarMorador();
				break;

			case 4:
				deletarMorador();
				break;

			case 5:
				inserirReserva();
				break;

			case 6:
				consultarReservas();
				System.out.println("Digite Sair para ir ao Menu:");
				String sairReserva = scanner.nextLine();
				break;

			case 7:
				atualizarReserva();
				break;

			case 8:
				deletarReserva();
				break;

			case 9:
				inserirPagamento();
				break;

			case 10:
				consultarPagamentos();
				System.out.println("Digite Sair para ir ao Menu:");
				String sairPagamento = scanner.nextLine();
				break;

			case 11:
				atualizarPagamento();
				break;

			case 12:
				deletarPagamento();
				break;

			case 13:
				System.out.println("Saindo do Programa!");
				return;

			default:
				System.out.println("Opção inválida. Tente novamente.");
				break;
			}
		}
	}
}
