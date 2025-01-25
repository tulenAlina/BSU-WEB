package by.bsu.lab4.filter;

import by.bsu.lab4.Entities.authentication.UserRole;
import by.bsu.lab4.contr.IController;
import by.bsu.lab4.services.AuthenticateService;
import by.bsu.lab4.Entities.authentication.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.IWebRequest;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebFilter(urlPatterns = "/*")
public class WelcomeFilter implements Filter{
    private JakartaServletWebApplication application;
    private ITemplateEngine templateEngine;
    private AuthenticateService authenticateService;




    @Override
    public void init(FilterConfig filterConfig) {

        this.application =
                JakartaServletWebApplication.buildApplication(filterConfig.getServletContext());

        this.templateEngine = buildTemplateEngine(this.application);
        this.authenticateService = new AuthenticateService();
    }

    @Override
    //фильтрацию всех get/post запросов 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();
        System.out.println(path);
        HttpSession session = httpRequest.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        UserRole userRole = user != null ? user.getRole() : UserRole.GUEST;
        boolean isPublicPage = path.startsWith("/home") || path.startsWith("/authPage") ||
                path.startsWith("/registerPage") || path.startsWith("/auth")||
                path.startsWith("/register") || path.equals("/");
        //проверяется роль пользователя
        if ((userRole == UserRole.GUEST && !isPublicPage))
        {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
        }

        if (!process((HttpServletRequest) request, (HttpServletResponse) response)) {
            chain.doFilter(request, response);
        }
    }

    private ITemplateEngine buildTemplateEngine(final IWebApplication application) {
        final WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);

        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(Long.valueOf(3600000L));
        templateResolver.setCacheable(true);
        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }

    public boolean process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
        	//Добавление пользователя в сессию
            var user = authenticateService.validateAndGetAuthUser(request, response);
            request.getSession(true).setAttribute("user", user);

            HttpSession session = request.getSession(true);
            Boolean cookiesUpdated = (Boolean) session.getAttribute("cookiesUpdated");

            Cookie[] cookies = request.getCookies();
            String lastVisit = null;
            int visitCount = 0;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("lastVisit".equals(cookie.getName())) {
                        lastVisit = cookie.getValue();
                    } else if ("visitCount".equals(cookie.getName())) {
                        visitCount = Integer.parseInt(cookie.getValue());
                    }
                }
            }

            request.setAttribute("lastVisit", lastVisit);
            request.setAttribute("visitCount", visitCount);

            if (cookiesUpdated == null || !cookiesUpdated) {
                visitCount++;

                Cookie visitCountCookie = new Cookie("visitCount", String.valueOf(visitCount));
                visitCountCookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
                response.addCookie(visitCountCookie);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String currentVisit = dateFormat.format(new Date());
                Cookie lastVisitCookie = new Cookie("lastVisit", currentVisit);
                lastVisitCookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
                response.addCookie(lastVisitCookie);

                session.setAttribute("cookiesUpdated", true);
            }

            final IServletWebExchange webExchange = this.application.buildExchange(request, response);
            final IWebRequest webRequest = webExchange.getRequest();
            final Writer writer = response.getWriter();

            IController controller = ControllerMappings.resolveControllerForRequest(webRequest);

            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            controller.process(webExchange, templateEngine, writer, request, response);
            return true;
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (final IOException ignored) {
                // Just ignore this
            }
            throw new ServletException(e);
        }
    }

}