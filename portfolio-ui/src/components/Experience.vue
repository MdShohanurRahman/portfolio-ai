<script>
export default {
  name: "Experience",
  props: {
    experiences: {
      type: Array,
      required: true
    }
  },
}
</script>

<template>
  <section id="experience" class="py-12">
    <h2 class="text-3xl font-bold text-cyan-400 mb-6">// Working Experience</h2>
    <div class="code-block space-y-4">
      <div v-for="(experience, index) in experiences" :key="index"
           class="experience-card p-6 rounded-xl bg-gray-800/50 border border-gray-700 hover:border-cyan-400/30 transition-all mb-6 group">

        <!-- Header with position and company -->
        <div class="flex items-start">
          <div class="bg-cyan-400/10 p-2 rounded-lg mr-4 group-hover:bg-cyan-400/20 transition">
            <i class="fas fa-briefcase text-cyan-400 text-lg"></i>
          </div>
          <div>
            <h3 class="text-xl font-semibold text-gray-100 group-hover:text-cyan-300 transition-colors">
              {{ experience.position }}
            </h3>
            <p class="text-gray-300 font-medium mt-1">
              {{ experience.company }}
              <span v-if="experience.location" class="text-gray-400">• {{ experience.location }}</span>
            </p>
          </div>
        </div>

        <!-- Duration and employment type -->
        <div class="flex flex-wrap items-center gap-4 mt-3 pl-12">
          <div class="flex items-center text-gray-400 text-sm">
            <i class="fas fa-calendar-alt text-cyan-400 mr-2"></i>
            {{ experience.duration }}
          </div>
          <div v-if="experience.employmentType" class="flex items-center text-gray-400 text-sm">
            <i class="fas fa-user-tie text-cyan-400 mr-2"></i>
            {{ experience.employmentType }}
          </div>
        </div>

        <!-- Contributions -->
        <ul class="space-y-2 mt-4 pl-12">
          <li v-for="(responsibility, index) in experience.contributions" :key="index"
              class="flex items-start text-gray-300">
            <span class="text-cyan-400 mr-2 mt-1">•</span>
            <span>{{ responsibility }}</span>
          </li>
        </ul>

        <!-- Skills/Tools -->
        <div v-if="experience.skills" class="flex flex-wrap gap-2 mt-4 pl-12">
    <span v-for="(skill, index) in experience.skills" :key="index"
          class="text-xs px-3 py-1 bg-gray-700 rounded-full text-cyan-400">
      {{ skill }}
    </span>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>

</style>