<script setup>
import { getData, url } from "../composable/getData";
import { ref, onBeforeMount, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAnnounceStore } from "@/stores/announce";
const announcer = useAnnounceStore();
const router = useRouter();
const data = ref([]);
const isShow = ref(false);
const isCheck404 = ref(false);
const colseShow = ref(false);
const categories = ref(["ทั้งหมด","ทั่วไป", "ทุนการศึกษา", "หางาน", "ฝึกงาน"]);
let isAnnouncementActive = computed(() => announcer.mode === "active");
const toggleAnnouncementState = () => {
  isAnnouncementActive.value = !isAnnouncementActive.value
  announcer.page = 0
  currentPage.value = 1
  checkmode()
  updateMode()
};
let filteredData = ref([]);

const checkmode =()=>{
  if(isAnnouncementActive.value === false){
    announcer.mode = "active"
  }else if(isAnnouncementActive.value === true){
    announcer.mode = "close"
  }
}

const cate = ref("ทั้งหมด");

watch (cate, (newVal) => {
  if(newVal === "ทั่วไป"){
    announcer.category = "1"
    currentPage.value = 1
    updateMode()
  }
  else if(newVal === "ทุนการศึกษา"){
    announcer.category = "2"
    currentPage.value = 1
    updateMode()
  }
  else if(newVal === "หางาน"){
    announcer.category = "3"
    currentPage.value = 1
    updateMode()
  }
  else if(newVal === "ฝึกงาน"){
    announcer.category = "4"
    currentPage.value = 1
    updateMode()
  }
  else if(newVal === "ทั้งหมด"){
    announcer.category = ""
    currentPage.value = 1
    updateMode()
  }
})


watch (filteredData, (newVal) => {
  if(newVal.length === 0){
    isShow.value = false;
    colseShow.value = true;
  }
  else {
      isShow.value = true;
      colseShow.value = false;
  }
})



//  ของใหม่
const currentPage = ref(announcer.currentPage);
const totalPages = ref();
const visiblePageNumbers = computed(() => {
  const pageNumbers = [];
  const maxVisiblePageNumbers = 10;
  
  let startPageNumber = Math.max(1, currentPage.value - Math.floor(maxVisiblePageNumbers / 2));
  let endPageNumber = Math.min(startPageNumber + maxVisiblePageNumbers - 1, totalPages.value);
  if (endPageNumber - startPageNumber + 1 < maxVisiblePageNumbers) {
    startPageNumber = Math.max(1, endPageNumber - maxVisiblePageNumbers + 1);
  }
  for (let i = startPageNumber; i <= endPageNumber; i++) {
    pageNumbers.push(i);
  }
  return pageNumbers;
});


watch(currentPage, (newVal) => {
  announcer.page = newVal-1
  announcer.currentPage = newVal
  updateMode()
})

onBeforeMount(async () => {
  data.value = await announcer.getDataPage()
  if (data.value === 404) {
    isCheck404.value = true;
  }
  for (let i = 0; i < data.value.content.length; i++) {
    if (data.value.content[i].publishDate === null) {
      data.value.content[i].publishDate = "-";
    }
    if (data.value.content[i].closeDate === null) {
      data.value.content[i].closeDate = "-";
    }
    if (data.value.content[i].publishDate !== '-') {
      let date = new Date(data.value.content[i].publishDate);
      date = date.toLocaleString("en-UK", {
        day: "numeric",
        month: "short",
        year: "numeric",
        hour: "numeric",
        minute: "numeric",
      });
      data.value.content[i].publishDate = date;
    }
    if (data.value.content[i].closeDate !== '-') {
      let date1 = new Date(data.value.content[i].closeDate);
      date1 = date1.toLocaleString("en-UK", {
        day: "numeric",
        month: "short",
        year: "numeric",
        hour: "numeric",
        minute: "numeric",
      });
      data.value.content[i].closeDate = date1;
    }
  }

  const checkEmpty = () => {
    if (data.value.length == 0) {
      isShow.value = false;
      colseShow.value = true;
    } else {
      isShow.value = true;
      colseShow.value = false;
    }
  };
  checkEmpty();
  filteredData.value = data.value.content
  totalPages.value = data.value.totalPages;
});

let showTimeZone = ref();
showTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;


const gotoDetail = (id) => {
  router.push({ name: "UserDetail", params: { id: id } });
};

const updateMode = async() =>{
  data.value = await announcer.getDataPage()
  for (let i = 0; i < data.value.content.length; i++) {
    if (data.value.content[i].publishDate === null) {
      data.value.content[i].publishDate = "-";
    }
    if (data.value.content[i].closeDate === null) {
      data.value.content[i].closeDate = "-";
    }
    if (data.value.content[i].publishDate !== '-') {
      let date = new Date(data.value.content[i].publishDate);
      date = date.toLocaleString("en-UK", {
        day: "numeric",
        month: "short",
        year: "numeric",
        hour: "numeric",
        minute: "numeric",
      });
      data.value.content[i].publishDate = date;
    }
    if (data.value.content[i].closeDate !== '-') {
      let date1 = new Date(data.value.content[i].closeDate);
      date1 = date1.toLocaleString("en-UK", {
        day: "numeric",
        month: "short",
        year: "numeric",
        hour: "numeric",
        minute: "numeric",
      });
      data.value.content[i].closeDate = date1;
    }
  }
  filteredData.value = data.value.content
  totalPages.value = data.value.totalPages;
}

</script>

<template>
  <div class="min-h-screen flex flex-col">
    <div class="text-center mt-2 text-3xl font-bold">
      <h1>SIT Announcement System (SAS)</h1>
    </div>

    <div class="mt-4 flex flex-row ml-8">
      <p class="font-bold">Date/Time shown in Timezone :</p>
      <p class="ml-1">{{ showTimeZone }}</p>

      <div class="ml-auto mr-4">
        <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-6 rounded"
          @click="toggleAnnouncementState">
          {{
            isAnnouncementActive
            ? "Close Announcements"
            : "Active Announcements"
          }}
        </button>
      </div>
    </div>

    <div class="flex flex-row ml-8 items-center">
      <p class="font-bold mr-2">Choose Category:</p>
      <select v-model="cate" class="h-8 pl-12 pr-8 border">
        <option v-for="(category, index) in categories" :key="index">
          {{ category }}
        </option>
      </select>
    </div>

    <div v-show="colseShow">
      <div class="flex flex-col items-center justify-center">
        <div class="text-3xl font-bold mt-10">No Announcement</div>
      </div>
    </div>

    <!-- ACTIVE MODE -->
    <div class="mt-6" v-show="isShow" v-if="isAnnouncementActive">
      <table class="table-auto overflow-hidden flex items-center justify-center border-black text-lg ">
        <thead class="py-6">
          <tr class="table-row border">
            <th class="px-28 py-4">No.</th>
            <th class="px-4 text-left">Title</th>
            <th class="px-16">Category</th>

          </tr>
          <tr class="table-row border" v-for="(item, index) of filteredData" :key="index">
            <th class="py-5">
              {{ index + 1 }}
            </th>
            <th class="max-w-sm overflow-hidden text-left" style="word-wrap: break-word" @click="gotoDetail(item.id)">
              {{ item.announcementTitle }}
            </th>
            <th>
              {{ item.announcementCategory }}
            </th>

          </tr>
        </thead>
      </table>

      <!-- ของใหม่ -->
      <div v-if="totalPages > 1" class="flex justify-center items-center mt-8">
  <ul class="pagination flex space-x-2">
    <li v-if="currentPage > 1" @click="currentPage--">
      <a href="#" class="bg-gray-200 hover:bg-gray-400 rounded px-3 py-1">Prev</a>
    </li>
    <li v-for="pageNumber in visiblePageNumbers" :key="pageNumber" :class="{ active: pageNumber === currentPage }" @click="currentPage = pageNumber">
      <a href="#" :class="{ 'bg-gray-400': pageNumber === currentPage }" class="bg-gray-200 hover:bg-gray-400 rounded px-3 py-1">{{ pageNumber }}</a>
    </li>
    <li v-if="currentPage < totalPages" @click="currentPage++">
      <a href="#" class="bg-gray-200 hover:bg-gray-400 rounded px-3 py-1">Next</a>
    </li>
  </ul>
</div>


    </div>
    
    <!-- CLOSE MODE -->
    <div class="mt-6" v-show="isShow" v-else>
      <table class="table-auto overflow-hidden flex items-center justify-center border-black text-lg">
        <thead class="py-6">
          <tr class="table-row border">
            <th class="px-28 py-4">No.</th>
            <th class="px-4 text-left">Title</th>
            <th class="px-16">Category</th>
            <th class="px-20">Close Date</th>

          </tr>
          <tr class="table-row border " v-for="(item, index) of filteredData" :key="index">
            <th class="py-5">
              {{ index + 1 }}
            </th>
            <th class="max-w-sm overflow-hidden text-left" style="word-wrap: break-word" @click="gotoDetail(item.id)">
              {{ item.announcementTitle }}
            </th>
            <th>
              {{ item.announcementCategory }}
            </th>
            <th>
              {{ item.closeDate }}
            </th>

          </tr>
        </thead>
      </table>
    

      <!-- ของใหม่ -->
      <div v-if="totalPages > 1" class="flex justify-center items-center mt-8">
  <ul class="pagination flex space-x-2">
    <li v-if="currentPage > 1" @click="currentPage--">
      <a href="#" class="bg-gray-200 hover:bg-gray-400 rounded px-3 py-1">Prev</a>
    </li>
    <li v-for="pageNumber in visiblePageNumbers" :key="pageNumber" :class="{ active: pageNumber === currentPage }" @click="currentPage = pageNumber">
      <a href="#" :class="{ 'bg-gray-400': pageNumber === currentPage }" class="bg-gray-200 hover:bg-gray-400 rounded px-3 py-1">{{ pageNumber }}</a>
    </li>
    <li v-if="currentPage < totalPages" @click="currentPage++">
      <a href="#" class="bg-gray-200 hover:bg-gray-400 rounded px-3 py-1">Next</a>
    </li>
  </ul>
</div>
    

    </div>
  </div>



  
</template>

<style scoped></style>
