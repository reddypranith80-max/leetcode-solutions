class Solution {
    void segregate0and1(int[] arr) {
        // code here
        int n = arr.length;
        int newarr[] = new int[n];
        int zerocnt=0;
        int onecnt=0;
        
        for(int i=0;i<n;i++){
            if(arr[i] == 0)zerocnt++;
            else onecnt++;
        }
        for(int i=0;i<zerocnt;i++){
            newarr[i] = 0;
        }
        for(int i=zerocnt;i<n;i++){
            newarr[i] = 1;
        }
        
        for(int i=0;i<n;i++){
           arr[i] = newarr[i];
        }
        
    }
}



// using two pointer approach
class Solution {
    void segregate0and1(int[] arr) {
        // code here
        int n = arr.length;
        int newarr[] = new int[n];
        int i=0;
        int j=n-1;
        
       while(i<j){
           if(arr[i] == 0 && arr[j] == 0){
               i++;
           }
           else if(arr[i] == 1 && arr[j] == 1){
               j--;
           }
           else if(arr[i] == 0 && arr[j] == 1){
               i++;
               j--;
           }
           // if(arr[i] == 1 && arr[j] == 0)
           else{
               int temp = arr[i];
               arr[i] = arr[j];
               arr[j] = temp;
              i++;
              j--;
           }
       }
       
        
    }
}
