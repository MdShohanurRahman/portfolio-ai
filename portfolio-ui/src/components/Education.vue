<script>
export default {
  name: "Education",
  props: {
    educations: {
      type: Array,
      required: true
    }
  }
}
</script>

<template>
  <section id="education" class="py-12">
    <h2 class="text-3xl font-bold text-cyan-400 mb-6">// Educational Background</h2>
    <div class="code-block space-y-4">
      <div v-for="(education, index) in educations" :key="index"
           class="education-card p-6 rounded-xl bg-gray-800/50 border border-gray-700 hover:border-cyan-400/30 transition-all mb-6 group">

        <!-- Header with degree and institution -->
        <div class="flex items-start">
          <div class="bg-cyan-400/10 p-2 rounded-lg mr-4 group-hover:bg-cyan-400/20 transition">
            <i class="fas fa-graduation-cap text-cyan-400 text-lg"></i>
          </div>
          <div>
            <h3 class="text-xl font-semibold text-gray-100 group-hover:text-cyan-300 transition-colors">
              {{ education.degree }}
            </h3>
            <p class="text-gray-300 font-medium mt-1">
              {{ education.institution }}
            </p>
          </div>
        </div>

        <!-- Details grid -->
        <div class="space-y-2 mt-3 pl-12">
          <div v-if="education.gpa" class="flex items-center text-gray-400">
            <i class="fas fa-star text-cyan-400 mr-2 w-5"></i>
            <span>GPA: {{ education.gpa }}</span>
          </div>
          <div class="flex items-center text-gray-400">
            <i class="fas fa-calendar-alt text-cyan-400 mr-2 w-5"></i>
            <span>{{ education.duration }}</span>
          </div>
        </div>

        <!-- Description -->
        <p v-if="education.description" class="text-gray-300 mt-4 pl-12 leading-relaxed">
          {{ education.description }}
        </p>

        <!-- Optional link to credential -->
        <a v-if="education.link"
           :href="education.link"
           target="_blank"
           class="mt-4 pl-12 inline-flex items-center text-cyan-400 hover:text-cyan-300 transition-colors text-sm">
          <i class="fas fa-external-link-alt mr-2"></i>
          View credential
        </a>
      </div>
    </div>
  </section>
</template>

<style scoped>

</style>