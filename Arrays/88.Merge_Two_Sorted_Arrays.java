class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i=0;
        int j=0;
        int cnt=0;
        int res[] = new int[m+n];
        while(i<m && j<n){

            if(nums1[i] <= nums2[j]){
                res[cnt] = nums1[i];
                cnt++;
                i++;
            }else{
                res[cnt] = nums2[j];
                cnt++;
                j++;
            }
        }
            while(i<m){
                res[cnt] = nums1[i];
                cnt++;
                i++;
            }
            while(j<n){
                res[cnt] = nums2[j];
                cnt++;
                j++;  
            }
        for(int k=0;k<res.length;k++){
            nums1[k] = res[k];
        }

    }
}