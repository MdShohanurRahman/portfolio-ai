<script>
export default {
  name: "AboutMe",
  props: {
    aboutMe: {
      type: String,
      required: true
    }
  }
}
</script>

<template>
  <section id="about" class="py-12">
    <h2 class="text-3xl font-bold text-cyan-400 mb-6">// About Me</h2>
    <div class="code-block flex items-center space-x-6">
      <img alt="Profile Photo" class="w-32 h-32 rounded-full border-2 border-cyan-400" src="../assets/profile-photo.jpg" />
      <p class="text-gray-300">{{aboutMe}}</p>
    </div>
  </section>
</template>

<style scoped>

</style>