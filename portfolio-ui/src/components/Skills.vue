<script>
export default {
  name: "Skills",
  props: {
    skills: {
      type: Array,
      required: true
    }
  },
}
</script>

<template>
  <section id="skills" class="py-12">
    <h2 class="text-3xl font-bold text-cyan-400 mb-8">// Skills & Proficiency</h2>
    <div class="code-block p-6 sm:p-8 rounded-xl shadow-lg bg-gradient-to-br from-gray-800 to-gray-900 border border-gray-700/50">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 sm:gap-8">
        <div
            v-for="(skill, index) in skills"
            :key="index"
            class="skill-category group transition-all hover:scale-[1.02]"
        >
          <!-- Category Header -->
          <div class="flex items-center justify-center mb-4 sm:mb-6">
            <div class="w-8 h-8 flex items-center justify-center bg-cyan-400/10 rounded-lg mr-3 group-hover:bg-cyan-400/20 transition">
              <i :class="skill.icon || 'fas fa-code'" class="text-cyan-400 text-sm"></i>
            </div>
            <h3 class="text-lg sm:text-xl font-semibold text-cyan-400">{{ skill.category }}</h3>
          </div>

          <!-- Skills List -->
          <ul class="space-y-2 sm:space-y-3">
            <li
                v-for="(item, itemIndex) in skill.items"
                :key="itemIndex"
                class="skill-item flex items-center justify-between bg-gray-700/50 hover:bg-gray-700/70 px-3 py-2 sm:px-4 sm:py-2.5 rounded-lg shadow border border-gray-600/30 transition-all"
            >
              <div class="flex items-center">
                <span class="text-gray-200 text-sm sm:text-base">{{ item.name }}</span>
              </div>
              <div class="flex items-center">
            <span class="text-cyan-400 text-xs sm:text-sm font-medium">
              <span v-if="item.proficiency">{{ item.proficiency }},</span>
              {{ item.years }} {{ item.years === 1 ? 'Year' : 'Years' }}
            </span>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>

</style>