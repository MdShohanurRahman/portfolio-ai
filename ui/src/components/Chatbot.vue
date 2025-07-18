<template>
  <div class="chatbot-container">
    <div id="chatbotBox" class="chatbot-box" :class="{ hidden: !isOpen }">
      <div class="chatbot-header">
        <span>Shohanur's Assistant</span>
        <button class="text-white" @click="toggleChatbot" aria-label="Close Chatbot">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="chatbot-messages">
        <div class="message bot-message" v-html="formatMessage(`Hello! I'm your AI assistant. Ask me anything about shohanur's portfolio!`)"></div>
        <div v-for="(message, index) in messages" :key="index"
             :class="message.type === 'user' ? 'message user-message' : 'message bot-message'"
             v-html="formatMessage(message.text)">
        </div>
      </div>
      <ChatbotInput @send-message="handleMessage" :isLoading="isLoading"/>
    </div>
    <div class="chatbot-icon" @click="toggleChatbot" aria-label="Open Chatbot">
      <i class="fas fa-robot"></i>
    </div>
  </div>
</template>

<script>
import { marked } from 'marked';
import DOMPurify from 'dompurify';
import ChatbotInput from './ChatbotInput.vue';

export default {
  name: 'Chatbot',
  components: {
    ChatbotInput
  },
  data() {
    return {
      isOpen: false,
      messages: [],
      conversationId: crypto.randomUUID(),
      isLoading: false,
    };
  },
  methods: {
    toggleChatbot() {
      this.isOpen = !this.isOpen;
    },
    formatMessage(text) {
      if (!text) return '';
      return DOMPurify.sanitize(marked.parse(text));
    },
    handleMessage(message) {
      if (message.trim()) {
        // Add user message immediately
        this.messages.push({text: message, type: 'user'});
        this.scrollToBottom();

        // Make API call
        this.isLoading = true;
        fetch('http://localhost:8080/api/v1/ai/ask', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({message: message, conversationId: this.conversationId}),
        }).then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json();
        }).then(response => {
          // Add bot response from API
          this.messages.push({
            text: response?.reply || 'Thanks for your message! Check out the portfolio sections for more info.',
            type: 'bot'
          });
          this.scrollToBottom();
        }).catch(error => {
          console.error('API Error:', error);
          // Fallback to default message if API fails
          this.messages.push({
            text: 'Thanks for your message! Check out the portfolio sections for more info.',
            type: 'bot'
          });
          this.scrollToBottom();
        }).finally(() => {
          this.isLoading = false;
        });
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const messagesContainer = this.$el.querySelector('.chatbot-messages');
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
      });
    }
  },
}
</script>

<style scoped>
.chatbot-container {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
}

.chatbot-box {
  width: 400px;
  height: 500px;
  background: #050510;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  border: 1px solid rgba(0, 240, 255, 0.3);
}

.chatbot-box.hidden {
  display: none;
}

.chatbot-header {
  background: linear-gradient(90deg, #00f0ff, #ff00f0);
  color: #0a0a1a;
  padding: 15px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chatbot-messages {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
}

.message {
  margin-bottom: 15px;
  padding: 10px 15px;
  border-radius: 18px;
  max-width: 80%;
  animation: fadeIn 0.3s ease;
}

.bot-message {
  background: rgba(0, 240, 255, 0.1);
  color: #f0f0ff;
  margin-right: auto;
  border-bottom-left-radius: 5px;
}

.user-message {
  background: linear-gradient(90deg, #00f0ff, #ff00f0);
  color: #0a0a1a;
  margin-left: auto;
  border-bottom-right-radius: 5px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.chatbot-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #00f0ff, #ff00f0);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 0 20px rgba(0, 240, 255, 0.5);
  transition: all 0.3s ease;
  animation: pulse 2s infinite;
}

.chatbot-icon:hover {
  transform: scale(1.1);
  box-shadow: 0 0 30px rgba(0, 240, 255, 0.8);
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(0, 240, 255, 0.7);
  }
  70% {
    box-shadow: 0 0 0 15px rgba(0, 240, 255, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(0, 240, 255, 0);
  }
}
/* Code blocks */
.chatbot-messages pre {
  background: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  overflow-x: auto;
  margin: 10px 0;
}

/* Code inline */
.chatbot-messages code {
  background: #f5f5f5;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: monospace;
}

/* Blockquotes */
.chatbot-messages blockquote {
  border-left: 3px solid #ddd;
  padding-left: 15px;
  margin: 10px 0;
  color: #666;
}

/* Lists */
.chatbot-messages ul, .chatbot-messages ol {
  margin: 10px 0;
  padding-left: 20px;
}

.chatbot-messages li {
  margin: 5px 0;
}

/* Links */
.chatbot-messages a {
  color: #3498db;
  text-decoration: none;
}

.chatbot-messages a:hover {
  text-decoration: underline;
}

/* Headings */
.chatbot-messages h1, .chatbot-messages h2, .chatbot-messages h3 {
  margin: 15px 0 5px;
  color: #2c3e50;
}

.chatbot-messages h1 { font-size: 1.5em; }
.chatbot-messages h2 { font-size: 1.3em; }
.chatbot-messages h3 { font-size: 1.1em; }
</style>