import { defineStore } from 'pinia'
import { ref } from 'vue'
import { url } from '../composable/getData.js'
export const useAnnounceStore = defineStore('announcer', () => {
    const mode = ref("active")
    const category = ref("")
    const page  = ref(0)
    const size = ref(5)
    const currentPage = ref(1)
    const getDataPage = async () => {
        try {
          const res = await fetch(url+`/pages?mode=${mode.value}&category=${category.value}&page=${page.value}&size=${size.value}`);
          if(res.status === 404) {
            const data = res.status;
            return data;
          }
          if (res.ok) {
            const data = res.json();
            return data;
          } else {
            throw new error("Error, can't get data");
          }
        } catch (error) {
          console.log(error);
        }
      };

    return {mode , category , page, getDataPage,currentPage}
})