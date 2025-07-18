<template>
  <div v-if="isLoading" class="loading-text">
    <span class="dot">.</span>
    <span class="dot">.</span>
    <span class="dot">.</span>
    Processing
  </div>
  <div class="chatbot-input">
    <textarea
        ref="messageInput"
        v-model="message"
        type="text"
        :placeholder="'Type your message...'"
        lang="en"
        aria-label="Chatbot Input"
        rows="4"
        @keydown="handleKeyDown"
        class="input-field"
        :disabled="isLoading"
    />
  </div>
</template>

<script>
export default {
  name: 'ChatbotInput',
  props: {
    isLoading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      message: '',
      loadingInterval: null
    };
  },
  methods: {
    async sendMessage() {
      if (this.isLoading || !this.message.trim()) return;
      this.startLoading();
      try {
        this.$emit('send-message', this.message);
        this.message = '';
      } finally {
        this.stopLoading();
      }
    },
    handleKeyDown(e) {
      if (e.key === 'Enter' && e.shiftKey) {
        this.addNewLine();
      } else if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        this.sendMessage();
      }
    },

    addNewLine() {
      const textarea = this.$refs.messageInput;
      const startPos = textarea.selectionStart;
      const endPos = textarea.selectionEnd;

      this.message =
          this.message.substring(0, startPos) +
          '\n' +
          this.message.substring(endPos);

      this.$nextTick(() => {
        textarea.selectionStart = textarea.selectionEnd = startPos + 1;
      });
    },

    startLoading() {
      this.loadingInterval = setInterval(() => {
        // Add any loading animation logic here if needed
      }, 500);
    },

    stopLoading() {
      clearInterval(this.loadingInterval);
    },
  },
  beforeDestroy() {
    this.stopLoading();
  }
};
</script>

<style scoped>
.chatbot-input {
  padding: 15px;
  border-top: 1px solid rgba(0, 240, 255, 0.2);
  display: flex;
  align-items: center;
}

.input-field {
  flex: 1;
  padding: 10px 15px;
  border-radius: 25px;
  border: none;
  background: rgba(0, 240, 255, 0.1);
  color: #f0f0ff;
  outline: none;
  font-family: 'Courier New', monospace;
}

.input-field:focus {
  box-shadow: 0 0 5px rgba(0, 240, 255, 0.5);
}
/* Loading animation */
.loading-text {
  color: #666;
  font-style: italic;
  padding: 5px 0;
  display: flex;
  align-items: center;
  gap: 2px;
  font-size: 0.9em;
  height: 20px; /* Fixed height to prevent layout shift */
}

.dot {
  display: inline-block;
  animation: blink 1.4s infinite both;
  font-weight: bold;
  transform: translateY(-1px); /* Better vertical alignment */
  width: 4px; /* Fixed width for consistent spacing */
  text-align: center;
}

.dot:nth-child(1) { animation-delay: 0s; }
.dot:nth-child(2) { animation-delay: 0.2s; }
.dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes blink {
  0%, 100% {
    opacity: 0.2;
    transform: translateY(-1px) scale(0.8);
  }
  50% {
    opacity: 1;
    transform: translateY(-1px) scale(1.1);
  }
}
</style>


