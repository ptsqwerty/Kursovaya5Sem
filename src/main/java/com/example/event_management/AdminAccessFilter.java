package com.example.event_management;

import com.example.event_management.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class AdminAccessFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String uri = httpRequest.getRequestURI();
        Boolean isLoggedIn = (Boolean) (session != null ? session.getAttribute("isLoggedIn") : false);
        User currentUser = (User) (session != null ? session.getAttribute("currentUser") : null);

        // Проверяем доступ к защищённым маршрутам
        if (uri.startsWith("/admin") && (!isLoggedIn || currentUser == null ||
                currentUser.getRoles().stream().noneMatch(role -> role.getName().equalsIgnoreCase("ADMIN")))) {
            httpResponse.sendRedirect("/"); // Перенаправление на главную страницу
            return;
        }

        // Продолжаем выполнение запроса
        chain.doFilter(request, response);
    }
}
