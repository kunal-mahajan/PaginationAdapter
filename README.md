1. create a child class of DynamicLoadingListHelper
2. To organize a view extend the class with PaginationAdapter
3. protected void loadData(final int offset, final int limit) will called automatically once the loading view is visible
4. need to set the data once its downloaded throught dataLoaded(Arrays.asList(s), totalRecords);
5. It will automatically download if page is not loaded with the Request limit.
6. It work for horizontal and vertical scroll just pass true for vertical 
