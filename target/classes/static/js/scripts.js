document.addEventListener('DOMContentLoaded', function() {
    let eventChart;
    const userRole = document.body.getAttribute('data-role') || '';

    // Счётчик событий в таблице
    const eventCountElement = document.getElementById('eventCount');
    if (eventCountElement) {
        const eventCount = document.querySelectorAll('#eventTable tbody tr').length;
        eventCountElement.textContent = eventCount;
    }

    // Функция для построения гистограммы
    function buildChart(data) {
        const labels = data.map(item => item[0]);
        const counts = data.map(item => item[1]);
        const eventChartElement = document.getElementById('eventChart');
        if (!eventChartElement) return;
        const ctx = eventChartElement.getContext('2d');
        if (eventChart) {
            eventChart.destroy();
        }
        eventChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Количество событий',
                    data: counts,
                    backgroundColor: 'rgba(54, 162, 235, 0.6)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    x: { title: { display: true, text: 'Дата прибытия' } },
                    y: { beginAtZero: true, title: { display: true, text: 'Количество событий' } }
                }
            }
        });
    }

    // Обновление данных гистограммы
    function updateChart() {
        fetch('/events/stats')
            .then(response => response.json())
            .then(data => { buildChart(data); })
            .catch(error => { console.error('Ошибка при обновлении гистограммы:', error); });
    }

    // Первоначальная загрузка гистограммы
    updateChart();

    // Инициализация модального окна редактирования события один раз
    const editEventModalElement = document.getElementById('editEventModal');
    let editEventModal;
    if (editEventModalElement) {
        editEventModal = new bootstrap.Modal(editEventModalElement, {
            backdrop: 'static', // Опционально: предотвращает закрытие модала при клике на backdrop
            keyboard: false // Опционально: предотвращает закрытие модала при нажатии клавиши Esc
        });

        // Обработка кнопок редактирования события
        document.addEventListener('click', function(event) {
            const editButton = event.target.closest('.editEventButton');
            if (editButton) {
                event.preventDefault();
                const id = editButton.getAttribute('data-id');
                const name = editButton.getAttribute('data-name');
                const content = editButton.getAttribute('data-content');
                const departureCity = editButton.getAttribute('data-departure-city');
                const departureDate = editButton.getAttribute('data-departure-date');
                const arrivalCity = editButton.getAttribute('data-arrival-city');
                const arrivalDate = editButton.getAttribute('data-arrival-date');

                // Заполнение полей формы модального окна
                document.getElementById('editEventId').value = id;
                document.getElementById('editName').value = name;
                document.getElementById('editContent').value = content;
                document.getElementById('editDepartureCity').value = departureCity;
                document.getElementById('editDepartureDate').value = departureDate;
                document.getElementById('editArrivalCity').value = arrivalCity;
                document.getElementById('editArrivalDate').value = arrivalDate;

                // Показ модального окна
                editEventModal.show();
            }
        });

        // Обработка отправки формы редактирования события через AJAX
        const editEventForm = document.getElementById('editEventForm');
        if (editEventForm) {
            editEventForm.addEventListener('submit', function(event) {
                event.preventDefault();
                const formData = new FormData(editEventForm);
                const eventData = Object.fromEntries(formData.entries());
                fetch('/events/edit', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(eventData)
                })
                    .then(response => {
                        if (response.ok) {
                            return response.json();
                        } else {
                            return response.json().then(errorData => {
                                throw new Error(errorData.error || 'Ошибка при редактировании события');
                            });
                        }
                    })
                    .then(updatedEvent => {
                        const rows = document.querySelectorAll('#eventTable tbody tr');
                        rows.forEach(function(row) {
                            const cell = row.querySelector('td');
                            if (cell && cell.textContent == updatedEvent.id) {
                                row.innerHTML = `
                                    <td>${updatedEvent.id}</td>
                                    <td>${updatedEvent.name}</td>
                                    <td>${updatedEvent.content}</td>
                                    <td>${updatedEvent.departureCity}</td>
                                    <td>${updatedEvent.departureDate}</td>
                                    <td>${updatedEvent.arrivalCity}</td>
                                    <td>${updatedEvent.arrivalDate}</td>
                                    <td>
                                        ${userRole === 'ADMIN' || userRole === 'EDITOR' ? `
                                        <a href="#" class="btn btn-sm btn-warning editEventButton" data-id="${updatedEvent.id}" data-name="${updatedEvent.name}" data-content="${updatedEvent.content}" data-departure-city="${updatedEvent.departureCity}" data-departure-date="${updatedEvent.departureDate}" data-arrival-city="${updatedEvent.arrivalCity}" data-arrival-date="${updatedEvent.arrivalDate}">
                                            <i class="fas fa-edit"></i> Редактировать
                                        </a>
                                        <a href="/events/deleteEvent/${updatedEvent.id}" class="btn btn-sm btn-danger">
                                            <i class="fas fa-trash-alt"></i> Удалить
                                        </a>
                                        ` : ''}
                                    </td>
                                `;
                            }
                        });
                        editEventForm.reset();
                        editEventModal.hide();
                        updateChart();
                    })
                    .catch(error => {
                        console.error('Ошибка:', error);
                        document.getElementById('editEventError').style.display = 'block';
                        document.getElementById('editEventError').textContent = error.message;
                    });
            });
        }

        // Обработка закрытия модального окна редактирования для удаления backdrops
        editEventModalElement.addEventListener('hidden.bs.modal', function () {
            const backdrops = document.querySelectorAll('.modal-backdrop');
            backdrops.forEach(backdrop => backdrop.remove());
        });
    }

    // Обработка модального окна добавления события
    const addEventModalElement = document.getElementById('addEventModal');
    if (addEventModalElement) {
        const addEventForm = document.getElementById('addEventForm');
        if (addEventForm) {
            addEventForm.addEventListener('submit', function(event) {
                event.preventDefault();
                const formData = new FormData(addEventForm);
                const eventData = Object.fromEntries(formData.entries());
                fetch('/events/saveEvent', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(eventData)
                })
                    .then(response => {
                        if (response.ok) {
                            return response.json();
                        } else {
                            return response.json().then(errorData => {
                                throw new Error(errorData.error || 'Ошибка при добавлении события');
                            });
                        }
                    })
                    .then(newEvent => {
                        addEventToTable(newEvent);
                        addEventForm.reset();
                        const modalInstance = bootstrap.Modal.getInstance(addEventModalElement);
                        if (modalInstance) {
                            modalInstance.hide();
                        }
                        updateChart();
                    })
                    .catch(error => {
                        console.error('Ошибка:', error);
                        document.getElementById('addEventError').style.display = 'block';
                        document.getElementById('addEventError').textContent = error.message;
                    });
            });
        }
    }

    // Добавление нового события в таблицу
    function addEventToTable(event) {
        const tableBody = document.querySelector('#eventTable tbody');
        const newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>${event.id}</td>
            <td>${event.name}</td>
            <td>${event.content}</td>
            <td>${event.departureCity}</td>
            <td>${event.departureDate}</td>
            <td>${event.arrivalCity}</td>
            <td>${event.arrivalDate}</td>
            <td>
                ${userRole === 'ADMIN' || userRole === 'EDITOR' ? `
                <a href="#" class="btn btn-sm btn-warning editEventButton" data-id="${event.id}" data-name="${event.name}" data-content="${event.content}" data-departure-city="${event.departureCity}" data-departure-date="${event.departureDate}" data-arrival-city="${event.arrivalCity}" data-arrival-date="${event.arrivalDate}">
                    <i class="fas fa-edit"></i> Редактировать
                </a>
                <a href="/events/deleteEvent/${event.id}" class="btn btn-sm btn-danger">
                    <i class="fas fa-trash-alt"></i> Удалить
                </a>
                ` : ''}
            </td>
        `;
        tableBody.appendChild(newRow);
        const eventCount = document.querySelectorAll('#eventTable tbody tr').length;
        if (eventCountElement) {
            eventCountElement.textContent = eventCount;
        }
    }

    // Подсчёт количества событий по дате
    function getEventCountByDate() {
        const dateInput = document.getElementById('dateInput');
        const eventCountByDateElement = document.getElementById('eventCountByDate');
        if (!dateInput || !eventCountByDateElement) {
            return;
        }
        const date = dateInput.value;
        if (!date) {
            eventCountByDateElement.textContent = 'Пожалуйста, выберите дату.';
            return;
        }
        fetch(`/events/countByDate?date=${encodeURIComponent(date)}`)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return response.json().then(errorData => {
                        throw new Error(errorData.error || 'Ошибка при получении данных');
                    });
                }
            })
            .then(count => {
                eventCountByDateElement.textContent = `Количество событий: ${count}`;
            })
            .catch(error => {
                console.error('Ошибка:', error);
                eventCountByDateElement.textContent = `Ошибка: ${error.message}`;
            });
    }

    // Привязка функции к кнопке подсчёта
    const countByDateButton = document.getElementById('countByDateButton');
    if (countByDateButton) {
        countByDateButton.addEventListener('click', getEventCountByDate);
    }

    // **Обработка формы входа через AJAX**
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const username = document.getElementById('loginUsername').value;
            const password = document.getElementById('loginPassword').value;
            fetch('/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username: username, password: password })
            })
                .then(response => {
                    if (response.ok) {
                        return response.text();
                    } else {
                        return response.text().then(text => { throw new Error(text); });
                    }
                })
                .then(message => {
                    const loginModalElement = document.getElementById('loginModal');
                    if (loginModalElement) {
                        const loginModal = bootstrap.Modal.getInstance(loginModalElement);
                        if (loginModal) {
                            loginModal.hide();
                        }
                    }
                    window.location.reload();
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                    const loginError = document.getElementById('loginError');
                    if (loginError) {
                        loginError.style.display = 'block';
                        loginError.textContent = error.message;
                    }
                });
        });
    }

    // **Обработка формы регистрации через AJAX**
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const username = document.getElementById('registerUsername').value;
            const password = document.getElementById('registerPassword').value;
            fetch('/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username: username, password: password })
            })
                .then(response => {
                    if (response.ok) {
                        return response.text();
                    } else {
                        return response.text().then(text => { throw new Error(text); });
                    }
                })
                .then(message => {
                    const registerSuccess = document.getElementById('registerSuccess');
                    if (registerSuccess) {
                        registerSuccess.style.display = 'block';
                        registerSuccess.textContent = message;
                    }
                    const registerError = document.getElementById('registerError');
                    if (registerError) {
                        registerError.style.display = 'none';
                    }
                    setTimeout(function() {
                        const registerModalElement = document.getElementById('registerModal');
                        if (registerModalElement) {
                            const registerModal = bootstrap.Modal.getInstance(registerModalElement);
                            if (registerModal) {
                                registerModal.hide();
                            }
                        }
                    }, 2000);
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                    const registerError = document.getElementById('registerError');
                    if (registerError) {
                        registerError.style.display = 'block';
                        registerError.textContent = error.message;
                    }
                });
        });
    }
});
