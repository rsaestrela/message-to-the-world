<template>
  <div>
    <Message/>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import Message from '@/components/Message.vue'

export default {
  name: 'Root',
  components: {
    Message
  }
}
</script>

<style scoped lang="scss">

</style>
