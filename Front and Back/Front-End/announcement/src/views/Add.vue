<script setup>
import { ref, onMounted, computed } from "vue";
import { url } from "../composable/getData";
import { useRoute, useRouter } from 'vue-router'
const router = useRouter()
const data = ref([]);
const props = defineProps({
  announcement: { type: Object }
})
const updatedAnnouncement = ref([]);
const inputCategory = ref("")
const intCategory = () => {
  if (inputCategory.value === "ทั่วไป") {
    updatedAnnouncement.value.categoryId = 1;
  } else if (inputCategory.value === "ทุนการศึกษา") {
    updatedAnnouncement.value.categoryId = 2;
  } else if (inputCategory.value === "หางาน") {
    updatedAnnouncement.value.categoryId = 3;
  } else if (inputCategory.value === "ฝึกงาน") {
    updatedAnnouncement.value.categoryId = 4;
  }
}
onMounted(async () => {
  if (props.announcement === undefined) {
    updatedAnnouncement.value = {
      "announcementTitle": "",
      "announcementDescription": "",
      "publishDate": "",
      "closeDate": "",
      "announcementDisplay": "",
      "categoryId": 0
    }
  }
})


const categories = ref(["ทั่วไป", "ทุนการศึกษา", "หางาน", "ฝึกงาน"]);
const Display = {
  Y: "Y",
  N: "N"
}
const myCheckbox = ref(false);

// ดู checkbox ว่าติ๊กไหม
const checkDisplay = () => {
  if (myCheckbox.value === true) {
    updatedAnnouncement.value.announcementDisplay = Display.Y;
  } else {
    updatedAnnouncement.value.announcementDisplay = Display.N;
  }
  changeDate()
}
const PublishDate = ref();
const CloseDate = ref();
const PublishTime = ref();
const CloseTime = ref();
let datePublish = ref();
let dateClose = ref();


//ดูวันที่ ถ้าไม่ใส่ให้เป็น null ถ้าใส่ก็ไม่เปลี่ยนอะไร
const changeDate = () => {
  if (PublishDate.value === undefined) {
    updatedAnnouncement.value.publishDate = null;
  }
  if (CloseDate.value === undefined) {
    updatedAnnouncement.value.closeDate = null;
  }
  if (PublishDate.value !== undefined) {
    const date = new Date(`${PublishDate.value}T${PublishTime.value}:00`).toISOString();
    datePublish.value = date;
    updatedAnnouncement.value.publishDate = datePublish.value;
  }
  if (CloseDate.value !== undefined) {
    const date1 = new Date(`${CloseDate.value}T${CloseTime.value}:00`).toISOString();
    dateClose.value = date1;
    updatedAnnouncement.value.closeDate = dateClose.value;
  }
};

// เพิ่ม announce อันใหม่
const addNewAnnounce = async (newAnnounce) => {
  checkDisplay()
  try {
    const res = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newAnnounce)
    })
    if (res.status === 200) {
      const addedAnnounce = await res.json()
      data.value.push(addedAnnounce)
      router.push({ name: 'Main' })
    } else throw new Error('กรุณากรอกข้อมูลให้ครบถ้วน')
  } catch (error) {
    console.log(error)
    alert(error.message)
  }
}

const checkValidate = (updatedAnnouncement) =>{
  intCategory()
  if (updatedAnnouncement.announcementTitle === null) {
    alert("ใส่ข้อความ");
  } else if (updatedAnnouncement.announcementTitle.trim() === "") {
    alert("Title ห้ามเว้นว่าง");
  } else if (
    updatedAnnouncement.announcementTitle.length < 1 ||
    updatedAnnouncement.announcementTitle.length > 200
  ) {
    alert("ใส่ข้อความมากกว่า 1 ตัวอักษรแต่ไม่เกิน 200 ตัวอักษร");
  } else if (updatedAnnouncement.announcementDescription.trim() === "") {
    alert("Description ห้ามเว้นว่าง");
  } else if (
    updatedAnnouncement.announcementDescription.length < 1 ||
    updatedAnnouncement.announcementDescription.length > 10000
  ) {
    alert("ใส่ข้อความมากกว่า 1 ตัวอักษรแต่ไม่เกิน 10,000 ตัวอักษร");
  } else if (updatedAnnouncement.announcementDescription === "") {
    alert("กรุณากรอกข้อมูลให้ครบถ้วน");
  } 
  if(PublishDate.value != undefined){
    if(PublishTime.value === undefined){
      alert("กรุณากรอกเวลาPublish Dateให้ครบถ้วน")
    }
  }
  if(PublishTime.value != undefined){
      if(PublishDate.value === undefined){
        alert("กรุณากรอกวันที่Publish Dateให้ครบถ้วน")
      }
  }
    if(CloseDate.value != undefined){
    if(CloseTime.value === undefined){
      alert("กรุณากรอกเวลาClose Dateให้ครบถ้วน")
    }}
      if(CloseTime.value != undefined){
        if(CloseDate.value === undefined){
          alert("กรุณากรอกวันที่Close Dateให้ครบถ้วน")
        }}
        else{
          addNewAnnounce(updatedAnnouncement)
        }
        if(PublishDate.value != undefined && PublishTime.value != undefined && CloseDate.value != undefined && CloseTime.value != undefined){
          addNewAnnounce(updatedAnnouncement)
        }
}
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <div class="ml-20 mt-8">
      <h1 class="text-3xl font-bold">Announcement Detail:</h1>


      <div class="mt-8">
        <h3>Title</h3>
        <div class=" border rounded-lg mr-4">
          <textarea class="h-8 w-full" v-model="updatedAnnouncement.announcementTitle"></textarea>
        </div>
      </div>

      <div class="mt-2">
        <h3>Category</h3>
        <div class="border rounded-lg mr-4 inline-block">
          <select v-model="inputCategory" class="h-8 w-full pl-12 pr-8">
            <option v-for="(category, index) in categories" :key="index">{{ category }}</option>
          </select>
        </div>
      </div>

      <div class="mt-8">
        <h3>Description</h3>
        <div class=" border rounded-lg mr-4">
          <textarea class="h-40 w-full" v-model="updatedAnnouncement.announcementDescription"></textarea>
        </div>
      </div>

      <div class="mt-8">
        <h3>Publish Date</h3>
        <div class=" border rounded-lg inline-block mr-4">
          <input type="date" class="h-8 w-full" placeholder="dd/mm/yyyy" v-model="PublishDate" />
        </div>
        <div class=" border rounded-lg inline-block mr-4">
          <input type="time" class="h-8 w-full" step="60" v-model="PublishTime">
        </div>
      </div>


      <div class="mt-8">
        <h3>Close Date</h3>
        <div class=" border rounded-lg inline-block mr-4">
          <input type="date" class="h-8 w-full" placeholder="dd/mm/yyyy" v-model="CloseDate" />
        </div>
        <div class="border rounded-lg inline-block mr-4">
          <input type="time" class="h-8 w-full" step="60" v-model="CloseTime" />
        </div>
      </div>

      <div class="mt-8">
        <h3>Display</h3>
        <div>
          <input type="checkbox" id="myCheckbox" v-model="myCheckbox">
          <label class="ml-2">Check to show this announcement</label>
        </div>
      </div>



      <div class="mt-2">


        <button class="hover:bg-green-500 font-bold py-1 px-2 rounded bg-green-300 justify-center  w-20"
          @click="checkValidate(updatedAnnouncement)">
          Submit
        </button>

        <router-link :to="{ name: 'Main' }">
          <button class="hover:bg-red-500 font-bold py-1 px-2 rounded bg-red-300 justify-center ml-3 w-20">
            Cancle
          </button>
        </router-link>
      </div>









    </div>
  </div>
</template>

<style scoped>
.popup {
  z-index: 9999;
}
</style>
