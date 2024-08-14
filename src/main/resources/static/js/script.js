document.addEventListener('DOMContentLoaded', function() {
    const messageTextarea = document.getElementById('message');
    const sendButton = document.querySelector('.send-button');
    const chatBox = document.getElementById('chat-box');

    function appendMessage(htmlContent, type) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${type}-message`;

        const icon = document.createElement('i');
        icon.className = type === 'user' ? 'fa fa-user' : 'gpt-icon fa fa-robot';

        const contentDiv = document.createElement('div');
        contentDiv.className = 'content';
        contentDiv.innerHTML = htmlContent;  

        messageDiv.appendChild(icon);
        messageDiv.appendChild(contentDiv);

        chatBox.appendChild(messageDiv);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    function formatResponseToHtml(responseText) {
        let formattedText = responseText.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
        formattedText = formattedText.replace(/\* (.*?)(\n|$)/g, '<li>$1</li>');
        formattedText = formattedText.replace(/(<li>.*<\/li>)+/g, '<ul>$&</ul>');
        return formattedText.replace(/\n/g, '<br>'); 
    }

    function fetchGptResponse(userMessage) {
        const url = `/generative-ai/${encodeURIComponent(userMessage)}`;
        fetch(url)
            .then(response => response.text())
            .then(data => {
                const formattedHtml = formatResponseToHtml(data);
                appendMessage(formattedHtml, 'gpt');
            })
            .catch(error => {
                console.error('Error fetching GPT response:', error);
                appendMessage('Sorry, something went wrong.', 'gpt');
            });
    }

    sendButton.addEventListener('click', function() {
        const userMessage = messageTextarea.value.trim();
        if (userMessage) {
            appendMessage(userMessage, 'user');
            messageTextarea.value = '';

            fetchGptResponse(userMessage);
        }
    });

    messageTextarea.addEventListener('keydown', function(event) {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            sendButton.click();
        }
    });
});
