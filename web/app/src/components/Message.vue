<template>
  <div class="container">
    <div class="message-container">
      <span class="message">
          "It's incredible, that people think, it was incredible. Stephen King is a great and incredible character.
          How quickly one accepts the incredible if only one sees it enough! Believe the incredible, and you can do the impossible."
      </span>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  name: 'Message',
}
</script>

<style scoped lang="scss">

.container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 800px;
  background-color: transparent;
}

.message-container {
  width: 1200px;
  text-align: center;
  border-radius: 6px;
  background-color: white;
}

.message {
  font-size: 2rem;;
}

</style>