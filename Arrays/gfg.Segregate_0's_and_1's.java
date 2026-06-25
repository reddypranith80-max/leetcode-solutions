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
