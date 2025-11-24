document.addEventListener('DOMContentLoaded', function() {
    const API_BASE = '/mdrt-test/api';

    // Переключение вкладок
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            const tabId = button.getAttribute('data-tab');

            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));

            button.classList.add('active');
            document.getElementById(tabId).classList.add('active');

            // Автоматическая загрузка данных при переключении вкладок
            if (tabId === 'master') {
                loadMasterList();
            } else if (tabId === 'detail') {
                loadDetailList();
            } else if (tabId === 'logs') {
                loadLogs();
            }
        });
    });

    // Загрузка списка документов
    async function loadMasterList() {
        const masterList = document.getElementById('masterList');
        masterList.innerHTML = '<div class="empty-state">Загрузка документов...</div>';

        try {
            const response = await fetch(API_BASE + '/create_document/get_documents');
            if (!response.ok) throw new Error('Ошибка загрузки документов');

            const documents = await response.json();

            if (documents.length === 0) {
                masterList.innerHTML = '<div class="empty-state">Документы не найдены</div>';
                return;
            }

            masterList.innerHTML = `
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Номер документа</th>
                            <th>Дата документа</th>
                            <th>Сумма</th>
                            <th>Комментарий</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${documents.map(doc => `
                            <tr>
                                <td>${escapeHtml(doc.docNumber || '')}</td>
                                <td>${escapeHtml(doc.docDate || '')}</td>
                                <td>${escapeHtml(doc.totalAmount || '0')}</td>
                                <td>${escapeHtml(doc.comment || '')}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (error) {
            console.error('Error:', error);
            masterList.innerHTML = '<div class="empty-state">Ошибка загрузки документов</div>';
        }
    }

    // Загрузка списка спецификаций
    async function loadDetailList() {
        const detailList = document.getElementById('detailList');
        const filterDocId = document.getElementById('filterDocId').value;

        detailList.innerHTML = '<div class="empty-state">Загрузка спецификаций...</div>';

        try {
            let url = API_BASE + '/create_document/get_specific';
            if (filterDocId) {
                url += `?docId=${encodeURIComponent(filterDocId)}`;
            }

            const response = await fetch(url);
            if (!response.ok) throw new Error('Ошибка загрузки спецификаций');

            const specifications = await response.json();

            if (specifications.length === 0) {
                detailList.innerHTML = '<div class="empty-state">Спецификации не найдены</div>';
                return;
            }

            detailList.innerHTML = `
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Номер документа</th>
                            <th>Название спецификации</th>
                            <th>Сумма</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${specifications.map(spec => `
                            <tr>
                                <td>${escapeHtml(spec.docNumber || '')}</td>
                                <td>${escapeHtml(spec.name || '')}</td>
                                <td>${escapeHtml(spec.amount || '0')}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (error) {
            console.error('Error:', error);
            detailList.innerHTML = '<div class="empty-state">Ошибка загрузки спецификаций</div>';
        }
    }

    // Загрузка логов
    async function loadLogs() {
        const logsList = document.getElementById('logsList');
        logsList.innerHTML = '<div class="log-entry">Загрузка логов...</div>';

        try {
            const response = await fetch(API_BASE + '/create_document/get_logs');
            if (!response.ok) throw new Error('Ошибка загрузки логов');

            const logs = await response.json();

            if (logs.length === 0) {
                logsList.innerHTML = '<div class="log-entry">Логи не найдены</div>';
                return;
            }

            logsList.innerHTML = logs.map(log => `
                <div class="log-entry ${log.succsess === 0 ? 'success' : 'error'}">
                    <div class="log-timestamp">
                        ${new Date(log.timestamp).toLocaleString()} |
                        Таблица: ${escapeHtml(log.tableName || '')} |
                        Статус: ${log.succsess === 0 ? 'Успех' : 'Ошибка'}
                    </div>
                    <div class="log-message">
                        <strong>Сообщение:</strong> ${escapeHtml(log.message || '')}
                    </div>
                    <div class="log-message">
                        <strong>Данные:</strong> ${escapeHtml(log.recordData || '')}
                    </div>
                </div>
            `).join('');
        } catch (error) {
            console.error('Error:', error);
            logsList.innerHTML = '<div class="log-entry error">Ошибка загрузки логов</div>';
        }
    }

    // Кнопки обновления данных
    document.getElementById('refreshMaster').addEventListener('click', loadMasterList);
    document.getElementById('refreshDetail').addEventListener('click', loadDetailList);
    document.getElementById('refreshLogs').addEventListener('click', loadLogs);

    // Фильтрация спецификаций по документу
    document.getElementById('filterDocId').addEventListener('input', loadDetailList);

    // Обработчики форм для документов
    document.getElementById('addMasterForm').addEventListener('submit', function(e) {
        e.preventDefault();
        submitForm(this, API_BASE + '/create_document/add_document', 'документ');
    });

    document.getElementById('changeMasterForm').addEventListener('submit', function(e) {
        e.preventDefault();
        submitForm(this, API_BASE + '/create_document/change_document', 'документ');
    });

    document.getElementById('deleteMasterForm').addEventListener('submit', function(e) {
        e.preventDefault();
        submitForm(this, API_BASE + '/create_document/delete_document', 'документ');
    });

    // Обработчики форм для спецификаций
    document.getElementById('addDetailForm').addEventListener('submit', function(e) {
        e.preventDefault();
        submitForm(this, API_BASE + '/create_document/add_specific', 'спецификацию');
    });

    document.getElementById('changeDetailForm').addEventListener('submit', function(e) {
        e.preventDefault();
        submitForm(this, API_BASE + '/create_document/change_specific', 'спецификацию');
    });

    document.getElementById('deleteDetailForm').addEventListener('submit', function(e) {
        e.preventDefault();
        submitForm(this, API_BASE + '/create_document/delete_specific', 'спецификацию');
    });

    // Общая функция отправки форм
    async function submitForm(form, url, entityName) {
        const submitButton = form.querySelector('button[type="submit"]');
        const originalText = submitButton.textContent;

        try {
            submitButton.disabled = true;
            submitButton.textContent = 'Отправка...';

            const formData = new FormData(form);
            const params = new URLSearchParams();

            for (const [key, value] of formData.entries()) {
                if (value) params.append(key, value);
            }

            const response = await fetch(url + '?' + params.toString(), {
                method: 'POST'
            });

            if (!response.ok) throw new Error('Ошибка сети');

            const data = await response.json();
            showNotification(data.message, data.success);

            if (data.success) {
                form.reset();
                if (url.includes('master')) {
                    loadMasterList();
                } else if (url.includes('specific')) {
                    loadDetailList();
                }
                if (url.includes('add_document') || url.includes('delete_document') || url.includes('change_document')) {
                    loadLogs();
                }
            }
        } catch (error) {
            console.error('Error:', error);
            showNotification(`Произошла ошибка при выполнении операции с ${entityName}`, false);
        } finally {
            submitButton.disabled = false;
            submitButton.textContent = originalText;
        }
    }

    // Функция для показа уведомлений
    function showNotification(message, isSuccess) {
        const notification = document.getElementById('notification');
        const notificationMessage = document.getElementById('notificationMessage');

        notificationMessage.textContent = message;
        notification.className = `notification ${isSuccess ? 'success' : 'error'}`;

        // Автоматическое скрытие через 5 секунд
        setTimeout(() => {
            notification.classList.add('hidden');
        }, 5000);
    }

    // Закрытие уведомления
    document.getElementById('closeNotification').addEventListener('click', function() {
        document.getElementById('notification').classList.add('hidden');
    });

    // Функция экранирования HTML
    function escapeHtml(unsafe) {
        if (unsafe === null || unsafe === undefined) return '';
        return unsafe.toString()
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    // Загрузка данных при открытии страницы
    loadMasterList();
    loadDetailList();
    loadLogs();
});