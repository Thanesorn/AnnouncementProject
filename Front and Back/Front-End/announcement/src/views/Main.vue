<script setup>
import { getData, url } from "../composable/getData";
import { ref, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
const router = useRouter();
const data = ref([]);
const isShow = ref(false);
const isCheck404 = ref(false);
const colseShow = ref(false);
onBeforeMount(async () => {
  data.value = await getData();
  if (data.value === 404) {
    isCheck404.value = true;
  }
  for (let i = 0; i < data.value.length; i++) {
    if (data.value[i].publishDate === null) {
      data.value[i].publishDate = "-";
    }
    if (data.value[i].closeDate === null) {
      data.value[i].closeDate = "-";
    }
    if (data.value[i].publishDate !== "-") {
      let date = new Date(data.value[i].publishDate);
      date = date.toLocaleString("en-UK", {
        day: "numeric",
        month: "short",
        year: "numeric",
        hour: "numeric",
        minute: "numeric",
      });
      data.value[i].publishDate = date;
    }
    if (data.value[i].closeDate !== "-") {
      let date1 = new Date(data.value[i].closeDate);
      date1 = date1.toLocaleString("en-UK", {
        day: "numeric",
        month: "short",
        year: "numeric",
        hour: "numeric",
        minute: "numeric",
      });
      data.value[i].closeDate = date1;
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
});
let showTimeZone = ref();
showTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;

// HTTP DELETE request ถ้าสำเร็จจะลบข้อมูลจาก data array
const deleteData = async (deleteId) => {
  try {
    const res = await fetch(url + `/${deleteId}`, {
      method: "DELETE",
    });
    if (res.ok) {
      data.value = data.value.filter((datas) => {
        return datas.id !== deleteId;
      });
    } else {
      throw new Error("Error, cannot delete data!!");
    }
  } catch (err) {
    console.log(err);
    alert(err.message);
  }
};

// เป็น true ถ้า confirm delete
let isCheckDelete = ref(false);

// เก็ย ID ของตัวที่จะdelete
let deleteId = ref();

// เรียกตอนกด delete เปลี่ยน isCheckDeleteเป็น true กับ เอาค่าใส่ deleteid
const checkDelete = (check, deleteid) => {
  if (check === true) {
    isCheckDelete.value = true;
    deleteId.value = deleteid;
  }
};
// เรียกตอนปิดหน้า confirm delete
const closeCheckDelete = (check) => {
  if (check === false) {
    isCheckDelete.value = false;
  }
};

// เรียกตอนกดปุ่ม delete เรียกใช้ deleteData กับ deleteId และset isCheckDelete 
const deleteNow = (check) => {
  deleteData(deleteId.value);
  isCheckDelete.value = check;
};


// ไปหน้า detail ดูตาม ID
const gotoDetail = (id) => {
  router.push({ name: "Detail", params: { id: id } });
};
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
        <router-link :to="{ name: 'Add' }">
          <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            Add Announcement
          </button>
        </router-link>
      </div>
    </div>
    <div v-show="colseShow">
      <div class="flex flex-col items-center justify-center">
        <div class="text-3xl font-bold mt-10">No Announcement</div>
      </div>
    </div>
    <div class="mt-6" v-show="isShow">
      <table class="table-auto overflow-hidden flex items-center justify-center border-black text-lg">
        <thead class="py-6">
          <tr class="table-row border">
            <th class="px-28 py-4">No.</th>
            <th class="px-4 text-left">Title</th>
            <th class="px-16">Category</th>
            <th class="px-16">Publish Date</th>
            <th class="px-4">Close Date</th>
            <th class="px-12">Display</th>
            <th class="px-20">Action</th>
          </tr>
          <tr class="table-row border" v-for="(item, index) of data" :key="index">
            <th class="py-5">
              {{ index + 1 }}
            </th>
            <th class="max-w-sm overflow-hidden text-left" style="word-wrap: break-word">
              {{ item.announcementTitle }}
            </th>
            <th>
              {{ item.announcementCategory }}
            </th>
            <th>
              {{ item.publishDate }}
            </th>
            <th>
              {{ item.closeDate }}
            </th>
            <th>
              {{ item.announcementDisplay }}
            </th>
            <th>
              <button class="w-20 hover:bg-green-500 font-bold py-1 px-2 rounded bg-green-300 justify-center"
                @click="gotoDetail(item.id)">
                View
              </button>

              <button class="w-20 hover:bg-red-500 font-bold py-1 px-2 rounded bg-red-300 justify-center ml-4"
                @click="checkDelete(true, item.id)">
                Delete
              </button>
            </th>
          </tr>
        </thead>
      </table>
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
          <router-link :to="{ name: 'Main' }"><button
              class="text-center font-bold bg-gray-300 but text-gray-800 m-2 p-4 mb-4 text-2xl rounded-full hover:bg-red-400 transition duration-500 ease-in-out flex-col"
              @click="closeError">
              Close
            </button>
          </router-link>
        </div>
      </div>
    </div>
  </div>

  <div v-show="isCheckDelete">
    <div class="popup">
      <div class="fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-screen h-screen bg-black opacity-60"></div>
    </div>
    <div class="popup">
      <div class="fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-1/3 h-5/6 bg-white rounded-xl">
        <div class="top-10">
          <p class="text-black text-center text-2xl mt-16">
            Are you sure you want to delete this data?
          </p>
        </div>
        <div class="flex flex-col">
          <img class="w-1/3 m-auto mt-20" src="./../assets/Pic/Error.png" alt="" />
        </div>
        <div class="flex flex-row fixed bottom-10 left-1/2 -translate-x-1/2">
          <button
            class="text-center font-bold bg-gray-300 but text-gray-800 m-2 p-4 mb-4 text-2xl rounded-full hover:bg-red-400 transition duration-500 ease-in-out flex items-center justify-center"
            style="width: 100px" @click="deleteNow(false)">
            Yes
          </button>

          <button
            class="text-center font-bold bg-gray-300 but text-gray-800 m-2 p-4 mb-4 text-2xl rounded-full hover:bg-red-400 transition duration-500 ease-in-out flex items-center justify-center"
            style="width: 100px" @click="closeCheckDelete(false)">
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
