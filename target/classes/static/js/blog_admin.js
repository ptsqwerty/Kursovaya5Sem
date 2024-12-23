/**
 * Скрипт для административного блога.
 * <p>
 * Обрабатывает действия, связанные с добавлением, редактированием и удалением записей в блоге.
 * </p>
 */
document.addEventListener('DOMContentLoaded', function() {
    /**
     * Обработка модального окна добавления записи.
     */
    const addPostForm = document.getElementById('addPostForm');
    if (addPostForm) {
        addPostForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const formData = new FormData(addPostForm);
            const postData = Object.fromEntries(formData.entries());
            fetch('/blog/admin/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(postData)
            })
                .then(response => {
                    if (response.ok) {
                        return response.text();
                    } else {
                        return response.text().then(text => { throw new Error(text); });
                    }
                })
                .then(message => {
                    alert(message);
                    window.location.reload();
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                    document.getElementById('addPostError').style.display = 'block';
                    document.getElementById('addPostError').textContent = error.message;
                });
        });
    }

    /**
     * Обработка модального окна редактирования записи.
     */
    document.addEventListener('click', function(event) {
        if (event.target.closest('.editPostButton')) {
            const button = event.target.closest('.editPostButton');
            const id = button.getAttribute('data-id');
            const title = button.getAttribute('data-title');
            const content = button.getAttribute('data-content');
            document.getElementById('editPostId').value = id;
            document.getElementById('editTitle').value = title;
            document.getElementById('editContent').value = content;
        }
    });

    /**
     * Обработка формы редактирования записи.
     */
    const editPostForm = document.getElementById('editPostForm');
    if (editPostForm) {
        editPostForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const formData = new FormData(editPostForm);
            const postData = Object.fromEntries(formData.entries());

            // Убедитесь, что дата правильно передаётся
            if (!postData.publicationDate) {
                console.error('Дата публикации не указана');
                return;
            }

            fetch('/blog/admin/edit', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(postData)
            })
                .then(response => {
                    if (response.ok) {
                        return response.text();
                    } else {
                        return response.text().then(text => { throw new Error(text); });
                    }
                })
                .then(message => {
                    alert(message);
                    window.location.reload();
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                    document.getElementById('editPostError').style.display = 'block';
                    document.getElementById('editPostError').textContent = error.message;
                });
        });

    }

    /**
     * Обработка удаления записи.
     */
    document.addEventListener('click', function(event) {
        if (event.target.closest('.deletePostButton')) {
            const button = event.target.closest('.deletePostButton');
            const id = button.getAttribute('data-id');
            if (confirm('Вы уверены, что хотите удалить эту запись?')) {
                fetch('/blog/admin/delete/' + id, { method: 'DELETE' })
                    .then(response => {
                        if (response.ok) {
                            return response.text();
                        } else {
                            return response.text().then(text => { throw new Error(text); });
                        }
                    })
                    .then(message => {
                        alert(message);
                        window.location.reload();
                    })
                    .catch(error => {
                        console.error('Ошибка:', error);
                        alert('Ошибка при удалении записи: ' + error.message);
                    });
            }
        }
    });
});
