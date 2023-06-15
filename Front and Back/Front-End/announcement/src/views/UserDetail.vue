<script setup>
import { getDataById } from "../composable/getData";
import { ref, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAnnounceStore } from "@/stores/announce";
const announcer = useAnnounceStore();
const { params } = useRoute();
const router = useRouter();
const data = ref([]);
const isCheck = ref(false);
const isCheck404 = ref(false);
const isClosedDateVisible = ref();

if (announcer.mode === "active") {
  isClosedDateVisible.value = false;
} else {
  isClosedDateVisible.value = true;

}
onBeforeMount(async () => {
  data.value = await getDataById(parseInt(params.id));
  if (data.value === 500) {
    isCheck.value = true;
  }
  if (data.value === 404) {
    isCheck404.value = true;
  }

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



</script>

<template>
  <div class="min-h-screen flex flex-col">

    <div class="border my-4  w-12/12   ">

      <div class="font-bold">
        <div class="ml-20 mt-6 text-3xl font-bold">
          <h1>{{ data.announcementTitle }}</h1>
        </div>

      </div>
      <div class="ml-20  ">

        <p class="mt-5" style="display: inline-block;">{{ data.announcementCategory }}</p>


        <p class="mt-5 ml-4 pr-8" style="display: inline-block; float: right;" v-show="isClosedDateVisible">
          <span style="color: red;">Closed on : </span>{{ data.closeDate }}
        </p>




        <p class="mt-10 ">{{ data.announcementDescription }}</p>


        <div class="mt-10 mb-10">
          <router-link :to="{ name: 'UserMain' }">
            <button class="hover:bg-gray-500 font-bold py-1 px-2 rounded bg-gray-300 justify-center w-16">
              Back
            </button></router-link>





        </div>
      </div>
    </div>

  </div>

  <div v-show="isCheck404">
    <div class="popup">
      <div class="fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-screen h-screen bg-black opacity-60"></div>
    </div>
    <div class="popup">
      <div class="fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-1/3 h-5/6 bg-white rounded-xl">
        <div class="top-10">
          <p class="text-black text-center text-4xl mt-16">Error</p>
        </div>
        <div class="flex flex-col">
          <img class="w-1/3 m-auto mt-20" src="./../assets/Pic/Error.png" alt="" />
        </div>
        <div class="flex flex-col fixed bottom-10 left-1/2 -translate-x-1/2">
          <router-link :to="{ name: 'UserMain' }"><button
              class="text-center font-bold bg-gray-300 but text-gray-800 m-2 p-4 mb-4 text-2xl rounded-full hover:bg-gray-400 transition duration-500 ease-in-out flex-col"
              @click="closeError">
              Close
            </button>
          </router-link>
        </div>
      </div>
    </div>
  </div>

  <div v-show="isCheck">
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
          <router-link :to="{ name: 'UserMain' }"><button
              class="text-center font-bold bg-gray-300 but text-gray-800 m-2 p-4 mb-4 text-2xl rounded-full hover:bg-gray-400 transition duration-500 ease-in-out flex-col">
              Back
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
