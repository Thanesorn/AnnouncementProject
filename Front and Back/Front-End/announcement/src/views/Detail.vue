<script setup>
import { getDataById } from "../composable/getData";
import { ref, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
const { params } = useRoute();
const router = useRouter();
const data = ref([]);
const isCheck = ref(false);
const isCheck404 = ref(false);
onBeforeMount(async () => {
  data.value = await getDataById(parseInt(params.id));
  if (data.value === 500) {
    isCheck.value = true;
  }
  if (data.value === 404) {
    isCheck404.value = true;
  }
  // if (data.value.announcementDisplay === "N") {
  //   data.value.publishDate = "-";
  //   data.value.closeDate = "-";
  // } 

  if (data.value.publishDate === null) {
    data.value.publishDate = "-";
  } if (data.value.closeDate === null) {
    data.value.closeDate = "-";
  }

  if (data.value.publishDate !== "-") {
    let date = new Date(data.value.publishDate);
    date = date.toLocaleString("en-UK", {
      day: "numeric",
      month: "short",
      year: "numeric",
      hour: "numeric",
      minute: "numeric",
    });
    data.value.publishDate = date;
  }
  if (data.value.closeDate !== "-") {
    let date1 = new Date(data.value.closeDate);
    date1 = date1.toLocaleString("en-UK", {
      day: "numeric",
      month: "short",
      year: "numeric",
      hour: "numeric",
      minute: "numeric",
    });
    data.value.closeDate = date1;
  }
});
const closeError = () => {
  isCheck404.value = false;
};

// ไปหน้าเเก้ไขข้อมูล
const gotoEdit = (id) => {
  router.push({ name: 'Edit', params: { id: id } })
}
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <div class="ml-20 mt-8 text-3xl font-bold">
      <h1>Announcement Detail:</h1>
    </div>
    <div class="border my-5 w-11/12 mx-auto flex flex-row">
      <div class="font-bold">
        <h3 class="mt-5 ml-4">Title</h3>
        <h3 class="mt-5 ml-4">Category</h3>
        <h3 class="mt-5 ml-4">Description</h3>
        <h3 class="mt-5 ml-4">Publish Date</h3>
        <h3 class="mt-5 ml-4">Close Date</h3>
        <h3 class="mt-5 ml-4 mb-5">Display</h3>
      </div>
      <div class="ml-20 ann-item ">
        <p class="mt-5 ml-4">{{ data.announcementTitle }}</p>
        <p class="mt-5 ml-4">{{ data.announcementCategory }}</p>
        <p class="mt-5 ml-4">{{ data.announcementDescription }}</p>
        <p class="mt-5 ml-4">{{ data.publishDate }}</p>
        <p class="mt-5 ml-4">{{ data.closeDate }}</p>
        <p class="mt-5 ml-4 mb-5">{{ data.announcementDisplay }}</p>
      </div>
    </div>
    <div class="ml-20">
      <router-link :to="{ name: 'Main' }">
        <button class="hover:bg-gray-500 font-bold py-1 px-2 rounded bg-gray-300 justify-center w-16">
          Back
        </button></router-link>
      <!-- <Add :announcement="editAnnouncement" v-show="false"/> -->
      <button class="hover:bg-green-500 font-bold py-1 px-2 rounded bg-green-300 justify-center ml-3 w-16"
        @click="gotoEdit(data.id)">
        Edit
      </button>
    </div>
  </div>


  <!-- หน้า error 404 -->
  <div v-show="isCheck404">
    <div class="popup">
      <div class="fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-screen h-screen bg-black opacity-60"></div>
    </div>
    <div class="popup">
      <div class="fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-1/3 h-5/6 bg-white rounded-xl">
        <div class="top-10">
          <p class="text-black text-center text-4xl mt-16">No Announcement</p>
        </div>
        <div class="flex flex-col">
          <img class="w-1/3 m-auto mt-20" src="./../assets/Pic/Error.png" alt="" />
        </div>
        <div class="flex flex-col fixed bottom-10 left-1/2 -translate-x-1/2">
          <router-link :to="{ name: 'Main' }"><button
              class="text-center font-bold bg-gray-300 but text-gray-800 m-2 p-4 mb-4 text-2xl rounded-full hover:bg-gray-400 transition duration-500 ease-in-out flex-col">
              Close
            </button>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.popup {
  z-index: 9999;
}
</style>
