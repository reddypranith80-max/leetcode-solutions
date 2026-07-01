class Solution {
    public void sortColors(int[] nums) {
        int n = nums.length;
        int zcnt=0;
        int ocnt=0;
        int tcnt=0;

        for(int i=0;i<n;i++){
            if(nums[i] == 0){
                zcnt++;
            }else if(nums[i] == 1){
                ocnt++;
            }else{
                tcnt++;
            }
        }

        for(int i=0;i<zcnt;i++){
            nums[i] = 0;
        }
        for(int i=zcnt;i<(zcnt+ ocnt);i++){
            nums[i] = 1;
        }
        for(int i=(zcnt+ocnt);i<n;i++){
            nums[i] = 2;
        }
        
    }
}

class Solution {
    public void sortColors(int[] nums) {
        int n = nums.length;
        int low=0;
        int mid=0;
        int high=n-1;

        while(mid <= high){
            if(nums[mid] == 0){
                int temp = nums[low];
                nums[low] = nums[mid];
                nums[mid] = temp;
                mid++;
                low++;
            }else if(nums[mid] == 1){
                mid++;
            }
            else{
                int temp = nums[mid];
                nums[mid] = nums[high];
                nums[high] = temp;
                high--;
            }
        }

    }
}