<script>
export default {
  name: "Certificates",
  props: {
    certificates: {
      type: Array,
      required: true
    }
  },
}
</script>

<template>
  <section id="certificates" class="py-12">
    <h2 class="text-3xl font-bold text-cyan-400 mb-6">// Certificates</h2>
    <div class="code-block space-y-4">
      <div class="space-y-8">
        <div
            v-for="(certificate, index) in certificates"
            :key="index"
            class="certificate-item p-6 rounded-lg bg-gray-800/50 border border-gray-700 hover:border-cyan-500/30 transition-all"
        >
          <!-- Header with title and issuer -->
          <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-2 mb-3">
            <h3 class="text-lg sm:text-xl font-semibold text-gray-100 flex items-center">
              <i class="fas fa-certificate text-cyan-400 mr-3 text-xl"></i>
              {{ certificate.title }}
            </h3>
            <span class="text-sm sm:text-base text-cyan-400 bg-cyan-400/10 px-3 py-1 rounded-full">
        {{ certificate.issuer }}
      </span>
          </div>

          <!-- Details grid -->
          <div class="grid grid-cols-1 gap-4 mt-4">
            <div class="flex items-center text-gray-400">
              <i class="fas fa-calendar-day text-cyan-400 mr-2 w-5"></i>
              <span>Issued: {{ certificate.time }}</span>
            </div>

            <div class="flex items-center text-gray-400">
              <i class="fas fa-clock text-cyan-400 mr-2 w-5"></i>
              <span v-if="certificate.expires">Expires: {{ certificate.expires }}</span>
              <span v-else>No expiration</span>
            </div>
          </div>

          <!-- Description -->
          <p class="text-gray-300 mt-4 leading-relaxed" v-if="certificate.description">
            {{ certificate.description }}
          </p>

          <!-- Link with icon -->
          <a
              v-if="certificate.link"
              :href="certificate.link"
              target="_blank"
              class="mt-4 inline-flex items-center text-cyan-400 hover:text-cyan-300 transition-colors"
          >
            <i class="fas fa-external-link-alt mr-2"></i>
            View credential
          </a>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>

</style>