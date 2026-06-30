public class Solution {
    int countTriplets(int sum, int arr[]) {
        int n = arr.length;
      long total=0;
        Arrays.sort(arr);
        for(int i=0;i<n;i++){
            int l=i+1,r=n-1;
            while(l<r){
                if(arr[i]+arr[l]+arr[r]<sum){
                    total+=r-l;
                    l++;
                }else{
                    r--;
                }
            }
        }
        return (int)total;
        
    }
} {
    
}
