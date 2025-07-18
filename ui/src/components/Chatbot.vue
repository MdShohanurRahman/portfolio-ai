<template>
  <div class="chatbot-container">
    <div id="chatbotBox" class="chatbot-box" :class="{ hidden: !isOpen }">
      <div class="chatbot-header">
        <span>Shohanur's Assistant</span>
        <button class="chatbot-close-btn" @click="toggleChatbot" aria-label="Close Chatbot">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="chatbot-messages">
        <div class="message bot-message" v-html="greetingMessage"></div>
        <div v-for="(message, index) in messages" :key="index"
             :class="message.type === 'user' ? 'message user-message' : 'message bot-message'"
             v-html="message.text">
        </div>
        <div v-if="isLoading" class="loading-indicator">
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
      <ChatbotInput @send-message="handleMessage" :isLoading="isLoading"/>
    </div>
    <div class="chatbot-icon" @click="toggleChatbot" aria-label="Open Chatbot">
      <i class="fas fa-robot"></i>
      <div class="notification-badge" v-if="unreadMessages > 0">{{ unreadMessages }}</div>
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
      unreadMessages: 0
    };
  },
  computed: {
    greetingMessage() {
      return this.formatMessage(`Greetings! I'm here to assist you as your AI assistant. Feel free to inquire about Shohanur's portfolio or request to schedule a meeting with him`);
    }
  },
  watch: {
    isOpen(newVal) {
      if (newVal) {
        this.unreadMessages = 0;
      }
    },
    messages(newMessages) {
      if (!this.isOpen && newMessages.length > 0 && newMessages[newMessages.length - 1].type === 'bot') {
        this.unreadMessages++;
      }
    }
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
        this.messages.push({text: message, type: 'user'});
        this.scrollToBottom();

        this.isLoading = true;
        fetch('http://localhost:8080/api/v1/ai/ask', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({message: message, conversationId: this.conversationId}),
        }).then(response => {
          if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
          return response.json();
        }).then(response => {
          this.messages.push({
            text: this.formatMessage(response?.reply) || 'Thanks for your message! Check out the portfolio sections for more info.',
            type: 'bot'
          });
        }).catch(error => {
          console.error('API Error:', error);
          this.messages.push({
            text: 'Thanks for your message! Check out the portfolio sections for more info.',
            type: 'bot'
          });
        }).finally(() => {
          this.isLoading = false;
          this.scrollToBottom();
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
  transform: translateY(20px);
  opacity: 0;
  transition: all 0.3s cubic-bezier(0.68, -0.55, 0.27, 1.55);
}

.chatbot-box:not(.hidden) {
  transform: translateY(0);
  opacity: 1;
}

.chatbot-header {
  background: linear-gradient(90deg, #00f0ff, #ff00f0);
  color: #0a0a1a;
  padding: 15px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.chatbot-close-btn {
  background: transparent;
  border: none;
  color: #0a0a1a;
  font-size: 1.2rem;
  cursor: pointer;
  transition: transform 0.2s;
}

.chatbot-close-btn:hover {
  transform: scale(1.2);
}

.chatbot-messages {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
  scroll-behavior: smooth;
  background: linear-gradient(to bottom, #050510, #0a0a1a);
}

.message {
  margin-bottom: 15px;
  padding: 12px 16px;
  border-radius: 18px;
  max-width: 85%;
  animation: fadeIn 0.3s ease;
  line-height: 1.5;
  word-break: break-word;
}

.bot-message {
  background: rgba(0, 240, 255, 0.1);
  color: #f0f0ff;
  margin-right: auto;
  border-bottom-left-radius: 5px;
  border: 1px solid rgba(0, 240, 255, 0.2);
}

.user-message {
  background: linear-gradient(90deg, #00f0ff, #ff00f0);
  color: #0a0a1a;
  margin-left: auto;
  border-bottom-right-radius: 5px;
  font-weight: 500;
}

.loading-indicator {
  display: flex;
  justify-content: center;
  padding: 10px;
  margin: 10px 0;
}

.typing-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 12px;
  background: rgba(0, 240, 255, 0.1);
  border-radius: 18px;
  width: fit-content;
}

.typing-indicator span {
  display: inline-block;
  width: 8px;
  height: 8px;
  margin: 0 2px;
  background-color: #00f0ff;
  border-radius: 50%;
  animation: typing-bounce 1.4s infinite ease-in-out;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing-bounce {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-5px); }
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
  position: relative;
}

.chatbot-icon:hover {
  transform: scale(1.1);
  box-shadow: 0 0 30px rgba(0, 240, 255, 0.8);
}

.notification-badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background: #ff00f0;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.7rem;
  font-weight: bold;
  box-shadow: 0 0 10px rgba(255, 0, 240, 0.7);
  animation: pulse 1.5s infinite;
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

@keyframes pulse {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(255, 0, 240, 0.7);
  }
  70% {
    transform: scale(1.1);
    box-shadow: 0 0 0 10px rgba(255, 0, 240, 0);
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(255, 0, 240, 0);
  }
}

/* Enhanced Markdown Styling */
.chatbot-messages pre {
  background: rgba(0, 0, 0, 0.3);
  padding: 12px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 12px 0;
  border-left: 3px solid #00f0ff;
  font-family: 'Courier New', monospace;
  font-size: 0.9em;
}

.chatbot-messages code {
  background: rgba(0, 240, 255, 0.1);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  color: #00f0ff;
  font-size: 0.9em;
}

.chatbot-messages blockquote {
  border-left: 3px solid #ff00f0;
  padding-left: 15px;
  margin: 15px 0;
  color: #ccc;
  font-style: italic;
}

.chatbot-messages ul, .chatbot-messages ol {
  margin: 15px 0;
  padding-left: 25px;
}

.chatbot-messages li {
  margin: 8px 0;
}

.chatbot-messages a {
  color: #00f0ff;
  text-decoration: none;
  border-bottom: 1px dashed #00f0ff;
  transition: all 0.2s;
}

.chatbot-messages a:hover {
  color: #ff00f0;
  border-bottom-color: #ff00f0;
}

.chatbot-messages h1,
.chatbot-messages h2,
.chatbot-messages h3 {
  margin: 20px 0 10px;
  color: #f0f0ff;
  font-weight: 600;
}

.chatbot-messages h1 {
  font-size: 1.5em;
  border-bottom: 1px solid rgba(0, 240, 255, 0.3);
  padding-bottom: 5px;
}
.chatbot-messages h2 { font-size: 1.3em; }
.chatbot-messages h3 { font-size: 1.1em; }

.chatbot-messages img {
  max-width: 100%;
  border-radius: 8px;
  margin: 10px 0;
  border: 1px solid rgba(0, 240, 255, 0.3);
}
</style>