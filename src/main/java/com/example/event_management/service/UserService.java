package com.example.event_management.service;

import com.example.event_management.model.User;
import com.example.event_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Сервисный класс для управления сущностями {@link User}.
 * <p>
 * Предоставляет бизнес-логику для операций с пользователями, включая аутентификацию и авторизацию.
 * </p>
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Получает пользователя по его ID.
     *
     * @param id ID пользователя
     * @return объект {@link User}
     * @throws RuntimeException если пользователь не найден
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));
    }

    /**
     * Обновляет сущность пользователя.
     *
     * @param user объект {@link User} для обновления
     * @return обновленный объект {@link User}
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Удаляет пользователя по его ID.
     *
     * @param id ID пользователя для удаления
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        user.getRoles().clear();
        userRepository.save(user);
        userRepository.delete(user);
    }

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя для поиска
     * @return объект {@link User}
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Проверяет, действительны ли предоставленные учетные данные.
     *
     * @param username имя пользователя
     * @param password пароль
     * @return {@code true}, если учетные данные верны, иначе {@code false}
     */
    public boolean checkCredentials(String username, String password) {
        User user = findByUsername(username);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    /**
     * Сохраняет нового пользователя с закодированным паролем.
     *
     * @param user объект {@link User} для сохранения
     * @return сохраненный объект {@link User}
     */
    public User saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
