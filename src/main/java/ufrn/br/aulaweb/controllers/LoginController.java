package ufrn.br.aulaweb.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufrn.br.aulaweb.model.Usuario;

import java.io.IOException;

@RestController
public class LoginController {

    @RequestMapping("/dashboard")
    public void exibirDashboard(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        // Verificação de Autorização
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("<h1>Painel de Demandas</h1>");
        response.getWriter().println("<p>Bem-vindo, " + usuario.email() + "</p>");
        response.getWriter().println("<p>ID da sua Sessão RAM: " + session.getId() + "</p>");

        response.getWriter().println("<form action='/logout' method='POST'><button type='submit'>Sair</button></form>");
    }

    @RequestMapping("/login")
    public void processarLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String metodo = request.getMethod();

        if ("GET".equalsIgnoreCase(metodo)) {
            exibirFormularioLogin(response);
        } else if ("POST".equalsIgnoreCase(metodo)) {
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");

            if (autenticarNoBanco(email, senha)) {
                HttpSession session = request.getSession(true);

                request.changeSessionId();

                Usuario usuarioLogado = new Usuario(1L, "Administrador", email);
                session.setAttribute("usuarioLogado", usuarioLogado);

                response.setStatus(HttpServletResponse.SC_SEE_OTHER);
                response.setHeader("Location", "/dashboard");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.setContentType("text/html");
                response.getWriter().println(
                        "<html>" +
                                "<body>" +
                                "<h3>Credenciais invalidas.</h3>" +
                                "<a href='/login'>Tentar novamente</a>" +
                                "</body>" +
                                "</html>");
            }
        }
    }

    // Simulação do Banco de Dados
    private boolean autenticarNoBanco(String email, String senha) {
        // Em um sistema real, a senha estaria com hash (Bcrypt) e a busca seria via Repository
        return "admin@ufrn.br".equals(email) && "senha123".equals(senha);
    }

    private void exibirFormularioLogin(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h2>Login do Sistema</h2>");
        response.getWriter().println("<form action='/login' method='POST'>");
        response.getWriter().println("E-mail: <input type='text' name='email'><br>");
        response.getWriter().println("Senha: <input type='password' name='senha'><br>");
        response.getWriter().println("<button type='submit'>Entrar</button>");
        response.getWriter().println("</form>");
        response.getWriter().println("</body></html>");
    }


}