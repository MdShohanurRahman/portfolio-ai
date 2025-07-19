<script>
export default {
  name: "Blogs"
}
</script>

<template>
  <section id="blogs" class="py-12">
    <h2 class="text-3xl font-bold text-cyan-400 mb-6">// My Blogs</h2>
    <div class="code-block space-y-4">
      <div class="blog-card group p-6 rounded-xl bg-gradient-to-br from-gray-800/50 to-gray-900/50 border border-gray-700 hover:border-cyan-400/30 transition-all hover:shadow-lg hover:shadow-cyan-400/10">
        <!-- Title with decorative icon -->
        <div class="flex items-start mb-3">
          <div class="bg-cyan-400/10 p-2 rounded-lg mr-4 group-hover:bg-cyan-400/20 transition">
            <i class="fas fa-blog text-cyan-400 text-lg"></i>
          </div>
          <h3 class="text-xl font-semibold text-gray-100 group-hover:text-cyan-300 transition-colors">
            Building Scalable APIs with Node.js
          </h3>
        </div>

        <!-- Description -->
        <p class="text-gray-300/90 mb-4 pl-12 leading-relaxed">
          A deep dive into architectural patterns and best practices for building high-performance RESTful APIs that can handle millions of requests.
        </p>

        <!-- Metadata and link -->
        <div class="flex flex-col sm:flex-row sm:items-center justify-between pl-12 gap-3">
          <div class="flex items-center text-sm text-gray-400">
            <i class="far fa-calendar mr-2"></i>
            <span>Published: June 15, 2023</span>
          </div>
          <a href="https://blog.example.com" class="inline-flex items-center text-cyan-400 hover:text-cyan-300 transition-colors font-medium">
            Read Article
            <i class="fas fa-arrow-right ml-2 text-xs transition-transform group-hover:translate-x-1"></i>
          </a>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>

</style>