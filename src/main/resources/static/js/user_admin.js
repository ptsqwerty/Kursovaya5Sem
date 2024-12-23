// user_admin.js
document.addEventListener('DOMContentLoaded', function() {
    const userRole = document.body.getAttribute('data-role') || '';
    const editUserModal = document.getElementById('editUserModal');

    if (editUserModal) {
        const editUserForm = document.getElementById('editUserForm');

        editUserModal.addEventListener('show.bs.modal', function(event) {
            const button = event.relatedTarget;
            const userId = button.getAttribute('data-id');
            const username = button.getAttribute('data-username');
            const rolesData = button.getAttribute('data-roles');
            let roles;

            try {
                roles = JSON.parse(rolesData);
            } catch (e) {
                console.error('Ошибка при парсинге rolesData:', e);
                roles = [];
            }

            document.getElementById('editUserId').value = userId;
            document.getElementById('editUsername').value = username;

            const rolesContainer = document.getElementById('editUserRoles');
            rolesContainer.innerHTML = '';
            const rolesList = ["ADMIN", "EDITOR", "VIEWER"];

            rolesList.forEach(function(role) {
                const div = document.createElement('div');
                div.className = 'form-check';

                const input = document.createElement('input');
                input.className = 'form-check-input';
                input.type = 'radio';
                input.name = 'role';
                input.id = 'role_' + role;
                input.value = role;

                if (roles.includes(role)) {
                    input.checked = true;
                }

                const label = document.createElement('label');
                label.className = 'form-check-label';
                label.htmlFor = 'role_' + role;
                label.textContent = role;

                div.appendChild(input);
                div.appendChild(label);
                rolesContainer.appendChild(div);
            });
        });

        if (editUserForm) {
            editUserForm.addEventListener('submit', function(event) {
                event.preventDefault();

                const userId = document.getElementById('editUserId').value;
                const role = document.querySelector('input[name="role"]:checked').value;

                fetch('/admin/users/edit', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ id: userId, roles: [role] })
                })
                    .then(response => {
                        if (response.ok) {
                            return response.json();
                        } else {
                            return response.json().then(errorData => {
                                throw new Error(errorData.error || 'Ошибка при редактировании пользователя');
                            });
                        }
                    })
                    .then(updatedUser => {
                        const rows = document.querySelectorAll('#manageUsersTable tbody tr');
                        rows.forEach(function(row) {
                            const cell = row.querySelector('td');
                            if (cell && cell.textContent == updatedUser.id) {
                                row.innerHTML = `
                                <td>${updatedUser.id}</td>
                                <td>${updatedUser.username}</td>
                                <td>${updatedUser.roles.map(role => role.name).join(', ')}</td>
                                <td>
                                    ${userRole === 'ADMIN' || userRole === 'EDITOR' ? `
                                    <button class="btn btn-sm btn-warning editUserButton" 
                                            data-id="${updatedUser.id}" 
                                            data-username="${updatedUser.username}" 
                                            data-roles='["${updatedUser.roles.map(role => role.name).join('","')}"]' 
                                            data-bs-toggle="modal" data-bs-target="#editUserModal">
                                        <i class="fas fa-edit"></i> Редактировать
                                    </button>
                                    <button class="btn btn-sm btn-danger deleteUserButton" data-id="${updatedUser.id}">
                                        <i class="fas fa-trash-alt"></i> Удалить
                                    </button>
                                    ` : ''}
                                </td>
                            `;
                            }
                        });
                        editUserForm.reset();
                        const modalInstance = bootstrap.Modal.getInstance(editUserModal);
                        modalInstance.hide();
                    })
                    .catch(error => {
                        console.error('Ошибка:', error);
                        const editUserError = document.getElementById('editUserError');
                        if (editUserError) {
                            editUserError.style.display = 'block';
                            editUserError.textContent = error.message;
                        }
                    });
            });
        }
    }

    const manageUsersTableBody = document.getElementById('manageUsersTableBody');
    if (manageUsersTableBody) {
        manageUsersTableBody.addEventListener('click', function(event) {
            if (event.target.closest('.deleteUserButton')) {
                event.preventDefault();
                const button = event.target.closest('.deleteUserButton');
                const userId = button.getAttribute('data-id');

                if (confirm('Вы уверены, что хотите удалить этого пользователя?')) {
                    fetch('/admin/users/delete/' + userId, { method: 'DELETE' })
                        .then(response => {
                            if (response.ok) {
                                return response.json();
                            } else {
                                return response.json().then(errorData => {
                                    throw new Error(errorData.error || 'Ошибка при удалении пользователя');
                                });
                            }
                        })
                        .then(data => {
                            alert(data.message || 'Пользователь успешно удалён');
                            const row = button.closest('tr');
                            if (row) {
                                row.remove();
                            }
                        })
                        .catch(error => {
                            console.error('Ошибка:', error);
                            alert('Ошибка при удалении пользователя: ' + error.message);
                        });
                }
            }
        });
    }
});
