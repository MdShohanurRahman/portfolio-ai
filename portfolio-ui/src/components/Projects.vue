<script>
export default {
  name: "Projects",
  props: {
    projects: {
      type: Array,
      required: true
    }
  },
}
</script>

<template>
  <section id="projects" class="py-12">
    <h2 class="text-3xl font-bold text-cyan-400 mb-6">// Projects</h2>
    <div class="code-block space-y-6"> <!-- Increased spacing between projects -->
      <div v-for="(project, index) in projects" :key="index"
           class="project-card p-6 rounded-xl bg-gradient-to-br from-gray-800/30 to-gray-900/30 border border-gray-700 hover:border-cyan-400/50 transition-all group mb-8 hover:shadow-lg hover:shadow-cyan-400/10">

        <!-- Project Header with Icon -->
        <div class="flex items-start">
          <div class="bg-cyan-400/10 p-2 rounded-lg mr-4 group-hover:bg-cyan-400/20 transition">
            <i class="fas fa-code text-cyan-400 text-lg"></i>
          </div>
          <div>
            <h3 class="text-xl font-semibold text-gray-100 group-hover:text-cyan-300 transition-colors">
              {{ project.title }}
            </h3>
            <p v-if="project.subtitle" class="text-gray-400 text-sm mt-1">
              {{ project.subtitle }}
            </p>
          </div>
        </div>

        <!-- Project Description -->
        <p class="text-gray-300 mt-3 pl-12 leading-relaxed">
          {{ project.description }}
        </p>

        <!-- Technologies Section -->
        <div class="mt-4 pl-12">
          <span class="text-sm font-medium text-gray-400">Built with:</span>
          <div class="flex flex-wrap gap-2 mt-2">
      <span
          v-for="(tool, index) in project.toolsUtilized"
          :key="index"
          class="inline-flex items-center text-xs px-3 py-1 bg-gray-700/70 rounded-full hover:bg-cyan-800/50 transition-colors border border-gray-600 group-hover:border-cyan-400"
      >
        {{ tool }}
      </span>
          </div>
        </div>

        <!-- Project Links -->
        <div class="mt-5 pl-12 flex flex-wrap gap-4">
          <a v-if="project?.links?.github"
             class="text-cyan-400 hover:text-cyan-300 transition flex items-center text-sm"
             :href="project.links.github" target="_blank">
            <i class="fab fa-github mr-1.5"></i> View Code
          </a>
          <a v-if="project?.links?.live"
             class="text-cyan-400 hover:text-cyan-300 transition flex items-center text-sm"
             :href="project.links.live" target="_blank">
            <i class="fas fa-external-link-alt mr-1.5"></i> Live Demo
          </a>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>

</style>